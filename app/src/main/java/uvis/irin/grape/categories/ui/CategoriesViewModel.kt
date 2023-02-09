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
            _viewState.update { loadingViewState() }

            loadCategories()
        }
    }

    private suspend fun loadCategories() {
        coroutineScope {
            val categories = fetchLocalCategoriesForPathUseCase(categoryPath)
            when {
                categories.isNotEmpty() -> {
                    _viewState.update {
                        viewStateForLoadedCategories(
                            categories = categories.map { it.toUiCategory() },
                            isSynchronizing = true
                        )
                    }

                    localImageLoadOfCategories(categories = categories)

                    synchronizeLocalCategoriesWithCloud()
                }
                else -> {
                    initialRemoteCategoryLoad()
                }
            }
        }
    }

    private suspend fun initialRemoteCategoryLoad() = coroutineScope {
        when (val result = fetchCategoriesForPathUseCase(categoryPath)) {
            is FetchCategoriesForPathResult.Success -> {
                val categoriesWithImagesDeferred = async {
                    categoriesWithImagesForFetchCategoriesForPathSuccess(result)
                }

                val categoriesWithoutImages =
                    categoriesWithoutImagesForFetchCategoriesForPathSuccess(result)

                _viewState.update {
                    viewStateForLoadedCategories(
                        categories = categoriesWithoutImages,
                        isSynchronizing = false,
                    )
                }

                val categoriesWithImages = categoriesWithImagesDeferred.await()

                _viewState.update {
                    viewStateForLoadedCategories(
                        categories = categoriesWithImages,
                        isSynchronizing = false,
                    )
                }
            }
            is FetchCategoriesForPathResult.Failure -> {
                _viewState.update { viewStateForFetchCategoriesForPathFailure(result) }
            }
        }
    }

    private suspend fun localImageLoadOfCategories(categories: List<DomainCategory>) =
        coroutineScope {
            val categoriesWithImages = categories.map { category ->
                val bitmap =
                    fetchLocalImageByteArrayForPathUseCase(category.path)?.let { byteArray ->
                        byteArrayToBitmap(byteArray)
                    }
                category.toUiCategory(bitmap = bitmap)
            }

            _viewState.update {
                viewStateForLoadedCategories(
                    categories = categoriesWithImages,
                    isSynchronizing = true
                )
            }
        }

    private suspend fun synchronizeLocalCategoriesWithCloud() = coroutineScope {
        val result = fetchCategoriesForPathUseCase(categoryPath)

        if (result is FetchCategoriesForPathResult.Success) {
            val categoriesWithImagesDeferred = async {
                categoriesWithImagesForFetchCategoriesForPathSuccess(result)
            }

            val categoriesWithImages = categoriesWithImagesDeferred.await()

            deleteRedundantLocalCategories(categoriesWithImages)

            _viewState.update {
                viewStateForLoadedCategories(
                    categories = categoriesWithImages,
                    isSynchronizing = false
                )
            }
        } else Unit
    }

    private fun categoriesWithoutImagesForFetchCategoriesForPathSuccess(
        result: FetchCategoriesForPathResult.Success
    ): List<UiCategory> {
        val categories = result.categories

        return categories.map { it.toUiCategory() }
    }

    private suspend fun categoriesWithImagesForFetchCategoriesForPathSuccess(
        result: FetchCategoriesForPathResult.Success
    ): List<UiCategory> {
        return coroutineScope {
            val categories = result.categories

            val fetchImageResults =
                categories.map { async { fetchImageByteArrayForPathUseCase(it.path) } }.awaitAll()

            val categoriesWithImages = categories.mapIndexed { index, category ->
                val fetchImageResult = fetchImageResults[index]

                if (fetchImageResult is FetchImageByteArrayForPathResult.Success) {
                    saveImageByteArrayLocallyUseCase(category.path, fetchImageResult.bytes)
                }
                val bitmap = bitmapForFetchImageByteArrayForPathResult(fetchImageResult)

                category.toUiCategory(
                    bitmap = bitmap,
                )
            }

            categoriesWithImages
        }
    }

    private suspend fun bitmapForFetchImageByteArrayForPathResult(
        result: FetchImageByteArrayForPathResult
    ): Bitmap {
        return (result as? FetchImageByteArrayForPathResult.Success)?.bytes.let { byteArray ->
            byteArrayToBitmap(byteArray)
        }
    }

    private suspend fun deleteRedundantLocalCategories(
        cloudCategories: List<UiCategory>
    ) {
        val domainCategories = cloudCategories.map { it.toDomainCategory() }

        deleteLocalCategoriesNotPresentInListUseCase(categoryPath, domainCategories)
    }

    private suspend fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap {
        return if (byteArray != null) {
            bitmapEncodingService.byteArrayToBitmap(byteArray)
        } else {
            bitmapEncodingService.xmlDrawableToBitmap(R.drawable.baseline_error_outline_24)
        }
    }

    private fun loadingViewState(): CategoriesViewState {
        return _viewState.value.copy(
            categoriesLoadingState = CategoriesLoadingState.Loading,
            categories = null,
            errorMessage = null,
        )
    }

    private fun viewStateForLoadedCategories(
        categories: List<UiCategory>,
        isSynchronizing: Boolean,
    ): CategoriesViewState {
        return _viewState.value.copy(
            categoriesLoadingState = CategoriesLoadingState.Loaded,
            isSynchronizing = isSynchronizing,
            categories = categories,
            errorMessage = null,
        )
    }

    private fun viewStateForFetchCategoriesForPathFailure(
        result: FetchCategoriesForPathResult.Failure
    ): CategoriesViewState {
        return _viewState.value.copy(
            categoriesLoadingState = CategoriesLoadingState.LoadingError,
            isSynchronizing = false,
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
