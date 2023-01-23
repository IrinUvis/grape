package uvis.irin.grape.categories.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uvis.irin.grape.navigation.CATEGORIES_ARG

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val categoryPath: String = checkNotNull(savedStateHandle[CATEGORIES_ARG])

    private val _viewState: MutableStateFlow<CategoriesViewState> = MutableStateFlow(
        CategoriesViewState()
    )
    val viewState: StateFlow<CategoriesViewState> = _viewState.asStateFlow()

    init {
        Log.d("VM", categoryPath)
    }
}
