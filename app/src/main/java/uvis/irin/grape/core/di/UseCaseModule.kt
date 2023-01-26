package uvis.irin.grape.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uvis.irin.grape.categories.domain.usecase.DeleteLocalCategoriesNotPresentInListUseCase
import uvis.irin.grape.categories.domain.usecase.FetchCategoriesForPathUseCase
import uvis.irin.grape.categories.domain.usecase.FetchImageByteArrayForPathUseCase
import uvis.irin.grape.categories.domain.usecase.FetchLocalCategoriesForPathUseCase
import uvis.irin.grape.categories.domain.usecase.FetchLocalImageByteArrayForPathUseCase
import uvis.irin.grape.categories.domain.usecase.SaveImageByteArrayLocallyUseCase
import uvis.irin.grape.categories.domain.usecase.impl.ProdDeleteLocalCategoriesNotPresentInListUseCase
import uvis.irin.grape.categories.domain.usecase.impl.ProdFetchCategoriesForPathUseCase
import uvis.irin.grape.categories.domain.usecase.impl.ProdFetchImageByteArrayForPathUseCase
import uvis.irin.grape.categories.domain.usecase.impl.ProdFetchLocalCategoriesForPathUseCase
import uvis.irin.grape.categories.domain.usecase.impl.ProdFetchLocalImageByteArrayForPathUseCase
import uvis.irin.grape.categories.domain.usecase.impl.ProdSaveImageByteArrayLocallyUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdFetchByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdFetchSoundsForPathUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindFetchSoundsForPathUseCase(
        fetchSoundsForPathUseCase: ProdFetchSoundsForPathUseCase,
    ): FetchSoundsForPathUseCase

    @Binds
    abstract fun bindFetchDownloadUrlForPathUseCase(
        fetchDownloadUrlForPathUseCase: ProdFetchByteArrayForPathUseCase,
    ): FetchByteArrayForPathUseCase

    @Binds
    abstract fun bindFetchCategoriesForPathUseCase(
        fetchCategoriesForPathUseCase: ProdFetchCategoriesForPathUseCase,
    ): FetchCategoriesForPathUseCase

    @Binds
    abstract fun bindFetchImageByteArrayForPathUseCase(
        fetchImageByteArrayForPathUseCase: ProdFetchImageByteArrayForPathUseCase,
    ): FetchImageByteArrayForPathUseCase

    @Binds
    abstract fun bindFetchLocalCategoriesForPathUseCase(
        fetchLocalCategoriesForPathUseCase: ProdFetchLocalCategoriesForPathUseCase,
    ): FetchLocalCategoriesForPathUseCase

    @Binds
    abstract fun bindFetchLocalImageByteArrayForPathUseCase(
        fetchLocalImageByteArrayForPathUseCase: ProdFetchLocalImageByteArrayForPathUseCase,
    ): FetchLocalImageByteArrayForPathUseCase

    @Binds
    abstract fun bindSaveImageByteArrayLocallyUseCase(
        saveImageByteArrayLocallyUseCase: ProdSaveImageByteArrayLocallyUseCase,
    ): SaveImageByteArrayLocallyUseCase

    @Binds
    abstract fun bindDeleteLocalCategoriesNotPresentInListUseCase(
        deleteLocalCategoriesNotPresentInListUseCase: ProdDeleteLocalCategoriesNotPresentInListUseCase
    ): DeleteLocalCategoriesNotPresentInListUseCase
}
