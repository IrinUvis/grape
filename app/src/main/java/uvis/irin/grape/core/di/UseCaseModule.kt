package uvis.irin.grape.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uvis.irin.grape.soundlist.domain.usecase.GetAllSoundsByCategoryUseCase
import uvis.irin.grape.soundlist.domain.usecase.GetSoundCategoriesUseCase
import uvis.irin.grape.soundlist.domain.usecase.ProdGetAllSoundsByCategoryUseCase
import uvis.irin.grape.soundlist.domain.usecase.ProdGetSoundCategoriesUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetAllSoundsByCategoryUseCase(
        getAllSoundsByCategoryUseCase: ProdGetAllSoundsByCategoryUseCase
    ): GetAllSoundsByCategoryUseCase

    @Binds
    abstract fun bindGetSoundCategoriesUseCase(
        getSoundCategoriesUseCase: ProdGetSoundCategoriesUseCase
    ): GetSoundCategoriesUseCase
}
