package uvis.irin.grape.soundlist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import uvis.irin.grape.soundlist.domain.repository.ProdSoundListRepository
import uvis.irin.grape.soundlist.domain.usecase.ProdGetAllSoundsByCategoryUseCase
import uvis.irin.grape.soundlist.domain.usecase.ProdGetSoundCategoriesUseCase

@Composable
fun SoundListScreen(
    viewModel: SoundListViewModel = SoundListViewModel(
        ProdGetSoundCategoriesUseCase(
            ProdSoundListRepository()
        ),
        ProdGetAllSoundsByCategoryUseCase(
            ProdSoundListRepository(),
            LocalContext.current
        )
    ),
) {
    val viewState = viewModel.viewState.collectAsState()

    SoundListContent(
        viewState = viewState.value
    )
}
