package uvis.irin.grape.categories.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
import uvis.irin.grape.core.extension.withItemAtIndex
import uvis.irin.grape.core.ui.helpers.UiText
import uvis.irin.grape.navigation.CATEGORIES_ARG
import uvis.irin.grape.navigation.INITIAL_CATEGORIES_ARG

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
                isFirstCategory = categoryPath == INITIAL_CATEGORIES_ARG,
                isFinalCategory = false,
                bitmap = bitmapEncodingService.drawableToBitmap(R.drawable.smutny_6)
            )
        )
    )
    val viewState: StateFlow<CategoriesViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            loadCategories()
        }
    }

    fun retryLoadingCategories() {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    categoriesLoadingState = CategoriesLoadingState.Loading,
                    errorMessage = null
                )
            }

            loadCategories()
        }
    }

    private suspend fun loadCategories() {
        coroutineScope {
            when (val useCaseResult = fetchCategoriesForPathUseCase(categoryPath)) {
                is FetchCategoriesForPathResult.Success -> {
                    val categories = useCaseResult.categories

                    _viewState.update {
                        it.copy(
                            categories = categories.map { category ->
                                category.toUiCategory(
                                    isFirstCategory = false,
                                    bitmap = bitmapEncodingService.drawableToBitmap(R.drawable.smutny_6)
                                )
                            },
                            categoriesLoadingState = CategoriesLoadingState.Loaded,
                        )
                    }

                    categories
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
                        .mapIndexed { index, pair ->
                            val bitmap = pair.first
                            val category = pair.second

                            val categoryWithImage = category.toUiCategory(
                                isFirstCategory = false,
                                bitmap = bitmap,
                            )

                            _viewState.update {
                                it.copy(
                                    categories = it.categories?.withItemAtIndex(
                                        item = categoryWithImage,
                                        index = index
                                    )
                                )
                            }
                        }
                }
                is FetchCategoriesForPathResult.Failure -> {
                    // Try loading them locally

                    _viewState.update {
                        viewStateForFetchSoundsForPathFailure(useCaseResult)
                    }
                }
            }
        }
    }

    private fun viewStateForFetchSoundsForPathFailure(
        result: FetchCategoriesForPathResult.Failure
    ): CategoriesViewState {
        return _viewState.value.copy(
            categoriesLoadingState = CategoriesLoadingState.LoadingError,
            errorMessage = errorMessageForFetchCategoriesForPathFailure(result)
        )
    }

    private fun errorMessageForFetchCategoriesForPathFailure(
        result: FetchCategoriesForPathResult.Failure,
    ): UiText {
        return when (result) {
            FetchCategoriesForPathResult.Failure.NoNetwork ->
                UiText.ResourceText(R.string.networkAndNoDownloadedCategoriesErrorMessage)
            FetchCategoriesForPathResult.Failure.ExceededFreeTier ->
                UiText.ResourceText(R.string.exceededFreeTierAndNoDownloadedCategoriesErrorMessage)
            FetchCategoriesForPathResult.Failure.Unexpected ->
                UiText.ResourceText(R.string.unexpectedAndNoDownloadedCategoriesErrorMessage)
            FetchCategoriesForPathResult.Failure.Unknown ->
                UiText.ResourceText(R.string.unknownAndNoDownloadedCategoriesErrorMessage)
        }
    }
}
