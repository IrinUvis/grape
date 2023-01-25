package uvis.irin.grape.categories.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uvis.irin.grape.R
import uvis.irin.grape.categories.domain.model.result.FetchCategoriesForPathResult
import uvis.irin.grape.categories.domain.model.result.FetchImageByteArrayForPathResult
import uvis.irin.grape.categories.domain.usecase.FetchCategoriesForPathUseCase
import uvis.irin.grape.categories.domain.usecase.FetchImageByteArrayForPathUseCase
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.categories.ui.model.toUiCategory
import uvis.irin.grape.core.android.service.image.BitmapEncodingService
import uvis.irin.grape.core.extension.withDashesReplacedByForwardSlashes
import uvis.irin.grape.navigation.CATEGORIES_ARG

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchCategoriesForPathUseCase: FetchCategoriesForPathUseCase,
    private val fetchImageByteArrayForPathUseCase: FetchImageByteArrayForPathUseCase,
    private val bitmapEncodingService: BitmapEncodingService,
) : ViewModel() {

    private val categoryPath: String = (checkNotNull(savedStateHandle[CATEGORIES_ARG]) as String)
        .withDashesReplacedByForwardSlashes()

    private val _viewState: MutableStateFlow<CategoriesViewState> = MutableStateFlow(
        CategoriesViewState(
            category = UiCategory(
                path = categoryPath,
                bitmap = bitmapEncodingService.drawableToBitmap(R.drawable.smutny_6)
            )
        )
    )
    val viewState: StateFlow<CategoriesViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("VM", categoryPath)

            val useCaseResult = fetchCategoriesForPathUseCase(categoryPath)

            if (useCaseResult is FetchCategoriesForPathResult.Success) {
                val categories = useCaseResult.categories

                val categoriesWithImages = categories
                    .map { category -> async { fetchImageByteArrayForPathUseCase(category.path) } }
                    .awaitAll()
                    .map { result ->
                        if (result is FetchImageByteArrayForPathResult.Success) {
                            bitmapEncodingService.byteArrayToBitmap(result.bytes)
                        } else {
                            bitmapEncodingService.drawableToBitmap(R.drawable.smutny_6)
                        }
                    }
                    .zip(categories)
                    .map { pair ->
                        val bitmap = pair.first
                        val category = pair.second

                        category.toUiCategory(bitmap)
                    }

                _viewState.update {
                    it.copy(
                        categories = categoriesWithImages,
                        isLoaded = true,
                    )
                }
            } else {
                // TODO: Failure
            }
        }
    }

    private suspend fun loadCategories() {

    }
}
