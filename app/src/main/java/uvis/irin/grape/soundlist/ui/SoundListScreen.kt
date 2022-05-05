package uvis.irin.grape.soundlist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import uvis.irin.grape.soundlist.domain.repository.ProdSoundListRepository
import uvis.irin.grape.soundlist.domain.usecase.ProdGetSoundCategoriesUseCase

@Composable
fun SoundListScreen(
    viewModel: SoundListViewModel = SoundListViewModel(ProdGetSoundCategoriesUseCase(
        ProdSoundListRepository())),
) {
    val viewState = viewModel.viewState.collectAsState()

    SoundListContent(
        viewState = viewState.value
    )
}
