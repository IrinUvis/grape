package uvis.irin.grape.categories.ui

import android.graphics.Bitmap
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
import uvis.irin.grape.categories.domain.model.DomainCategory
import uvis.irin.grape.categories.domain.model.result.FetchCategoriesForPathResult
import uvis.irin.grape.categories.domain.model.result.FetchImageByteArrayForPathResult
import uvis.irin.grape.categories.domain.usecase.DeleteLocalCategoriesNotPresentInListUseCase
import uvis.irin.grape.categories.domain.usecase.FetchCategoriesForPathUseCase
import uvis.irin.grape.categories.domain.usecase.FetchImageByteArrayForPathUseCase
import uvis.irin.grape.categories.domain.usecase.FetchLocalCategoriesForPathUseCase
import uvis.irin.grape.categories.domain.usecase.FetchLocalImageByteArrayForPathUseCase
import uvis.irin.grape.categories.domain.usecase.SaveImageByteArrayLocallyUseCase
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.categories.ui.model.toDomainCategory
import uvis.irin.grape.categories.ui.model.toUiCategory
import uvis.irin.grape.core.android.service.image.BitmapEncodingService
import uvis.irin.grape.core.extension.withDashesReplacedByForwardSlashes
import uvis.irin.grape.core.ui.helpers.UiText
import uvis.irin.grape.navigation.CATEGORIES_ARG
import uvis.irin.grape.navigation.INITIAL_CATEGORIES_ARG

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchCategoriesForPathUseCase: FetchCategoriesForPathUseCase,
    private val fetchImageByteArrayForPathUseCase: FetchImageByteArrayForPathUseCase,
    private val fetchLocalCategoriesForPathUseCase: FetchLocalCategoriesForPathUseCase,
    private val fetchLocalImageByteArrayForPathUseCase: FetchLocalImageByteArrayForPathUseCase,
    private val deleteLocalCategoriesNotPresentInListUseCase: DeleteLocalCategoriesNotPresentInListUseCase,
    private val saveImageByteArrayLocallyUseCase: SaveImageByteArrayLocallyUseCase,
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
                bitmap = null,
            )
        )
    )
    val viewState: StateFlow<CategoriesViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch { loadCategories() }
    }

    fun retryLoadingCategories() {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    categoriesLoadingState = CategoriesLoadingState.Loading,
                    categories = null,
                    errorMessage = null,
                )
            }

            initialRemoteCategoryLoad()
        }
    }

    private suspend fun loadCategories() = coroutineScope {
        val localCategories = fetchLocalCategoriesForPathUseCase(categoryPath)
        when {
            localCategories.isNotEmpty() -> {
                fullLocalCategoryLoadWithSynchronization(baseLocalCategories = localCategories)
            }
            else -> {
                initialRemoteCategoryLoad()
            }
        }
    }

    private suspend fun fullLocalCategoryLoadWithSynchronization(baseLocalCategories: List<DomainCategory>) =
        coroutineScope {
            // load initially with local categories without images
            _viewState.update {
                it.forLoadedCategories(
                    categories = baseLocalCategories.map { category -> category.toUiCategory() },
                    isSynchronizing = true,
                )
            }

            // load categories with images
            val localCategoriesWithImages = baseLocalCategories.map { category ->
                val imageByteArray = fetchLocalImageByteArrayForPathUseCase(category.path)
                val bitmap = byteArrayToBitmap(imageByteArray)
                category.toUiCategory(bitmap = bitmap)
            }

            _viewState.update {
                it.forLoadedCategories(
                    categories = localCategoriesWithImages,
                    isSynchronizing = true,
                )
            }

            // load remote categories to synchronize
            when (val fetchCategoriesResult = fetchCategoriesForPathUseCase(categoryPath)) {
                is FetchCategoriesForPathResult.Success -> {
                    val categoriesWithImages =
                        getCategoriesWithImagesLoadedRemotely(
                            categories = fetchCategoriesResult.categories.map { it.toUiCategory() }
                        )

                    deleteLocalCategoriesNotPresentInListUseCase(
                        path = categoryPath,
                        categoryList = categoriesWithImages.map { it.toDomainCategory() }
                    )

                    _viewState.update {
                        it.forLoadedCategories(
                            categories = categoriesWithImages,
                            isSynchronizing = false,
                        )
                    }
                }
                is FetchCategoriesForPathResult.Failure -> {
                    _viewState.update {
                        it.copy(
                            isSynchronizing = false
                        )
                    }
                }
            }
        }

    private suspend fun initialRemoteCategoryLoad() = coroutineScope {
        when (val result = fetchCategoriesForPathUseCase(categoryPath)) {
            is FetchCategoriesForPathResult.Success -> {
                val categoriesWithoutImages = result.categories.map { it.toUiCategory() }

                _viewState.update {
                    it.forLoadedCategories(
                        categories = categoriesWithoutImages,
                        isSynchronizing = false,
                    )
                }

                val categoriesWithImages = getCategoriesWithImagesLoadedRemotely(
                    categories = categoriesWithoutImages
                )

                _viewState.update {
                    it.forLoadedCategories(
                        categories = categoriesWithImages,
                        isSynchronizing = false,
                    )
                }
            }
            is FetchCategoriesForPathResult.Failure -> {
                _viewState.update {
                    it.copy(
                        categoriesLoadingState = CategoriesLoadingState.LoadingError,
                        isSynchronizing = false,
                        errorMessage = errorMessageForFetchCategoriesForPathFailure(result)
                    )
                }
            }
        }
    }

    private suspend fun getCategoriesWithImagesLoadedRemotely(
        categories: List<UiCategory>
    ): List<UiCategory> = coroutineScope {
        val fetchImageResults =
            categories.map { async { fetchImageByteArrayForPathUseCase(it.path) } }.awaitAll()

        val categoriesWithImages = categories.mapIndexed { index, category ->
            val fetchImageResult = fetchImageResults[index]

            if (fetchImageResult is FetchImageByteArrayForPathResult.Success) {
                saveImageByteArrayLocallyUseCase(category.path, fetchImageResult.bytes)
            }

            val bitmap = byteArrayToBitmap(
                byteArray = (fetchImageResult as? FetchImageByteArrayForPathResult.Success)?.bytes
            )

            category.copy(
                bitmap = bitmap,
            )
        }

        categoriesWithImages
    }

    private suspend fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap {
        return if (byteArray != null) {
            bitmapEncodingService.byteArrayToBitmap(byteArray)
        } else {
            bitmapEncodingService.xmlDrawableToBitmap(R.drawable.baseline_error_outline_24)
        }
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

private fun CategoriesViewState.forLoadedCategories(
    categories: List<UiCategory>,
    isSynchronizing: Boolean? = null,
) = copy(
    categoriesLoadingState = CategoriesLoadingState.Loaded,
    isSynchronizing = isSynchronizing ?: this.isSynchronizing,
    categories = categories,
    errorMessage = null,
)
