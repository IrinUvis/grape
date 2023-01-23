package uvis.irin.grape.categories.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CategoriesViewModel @Inject constructor() : ViewModel() {

    private val _viewState: MutableStateFlow<CategoriesViewState> = MutableStateFlow(
        CategoriesViewState()
    )
    val viewState: StateFlow<CategoriesViewState> = _viewState.asStateFlow()
}
