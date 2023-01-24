package uvis.irin.grape.categories.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.extension.withDashesReplacedByForwardSlashes
import uvis.irin.grape.navigation.CATEGORIES_ARG

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val categoryPath: String = (checkNotNull(savedStateHandle[CATEGORIES_ARG]) as String)
        .withDashesReplacedByForwardSlashes()

    private val _viewState: MutableStateFlow<CategoriesViewState> = MutableStateFlow(
        CategoriesViewState(
            category = UiCategory(
                path = categoryPath,
            )
        )
    )
    val viewState: StateFlow<CategoriesViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("VM", categoryPath)
            delay(2000)
            _viewState.update {
                val category = it.category
                it.copy(
                    categories = List(10) { category },
                    isLoaded = true,
                )
            }
        }
    }
}
