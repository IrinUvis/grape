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
import uvis.irin.grape.soundlist.domain.usecase.ClearCachedSoundsUseCase
import uvis.irin.grape.soundlist.domain.usecase.CreateCacheSoundFileUseCase
import uvis.irin.grape.soundlist.domain.usecase.DeleteLocalSoundsNotPresentInListUseCase
import uvis.irin.grape.soundlist.domain.usecase.DeleteSoundFileUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchLocalSoundFileForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchLocalSoundsForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.GetFileExistsUseCase
import uvis.irin.grape.soundlist.domain.usecase.SaveSoundLocallyUseCase
import uvis.irin.grape.soundlist.domain.usecase.ShareSoundUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdClearCachedSoundsUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdCreateCacheSoundFileUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdDeleteLocalSoundsNotPresentInListUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdDeleteSoundFileUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdFetchLocalSoundFileForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdFetchLocalSoundsForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdFetchSoundByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdFetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdGetFileExistsUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdSaveSoundLocallyUseCase
import uvis.irin.grape.soundlist.domain.usecase.impl.ProdShareSoundUseCase

@Suppress("TooManyFunctions")
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    // ------------- SOUNDS ----------------

    @Binds
    abstract fun bindClearCachedSoundsUseCase(
        clearCachedSoundsUseCase: ProdClearCachedSoundsUseCase
    ): ClearCachedSoundsUseCase

    @Binds
    abstract fun bindCreateCacheSoundFileUseCase(
        createCacheSoundFileUseCase: ProdCreateCacheSoundFileUseCase
    ): CreateCacheSoundFileUseCase

    @Binds
    abstract fun bindDeleteLocalSoundsNotPresentInListUseCase(
        deleteLocalSoundsNotPresentInListUseCase: ProdDeleteLocalSoundsNotPresentInListUseCase
    ): DeleteLocalSoundsNotPresentInListUseCase

    @Binds
    abstract fun bindDeleteSoundFileUseCase(
        deleteSoundFileUseCase: ProdDeleteSoundFileUseCase
    ): DeleteSoundFileUseCase

    @Binds
    abstract fun bindFetchLocalSoundFileForPathUseCase(
        fetchLocalSoundFileForPathUseCase: ProdFetchLocalSoundFileForPathUseCase
    ): FetchLocalSoundFileForPathUseCase

    @Binds
    abstract fun bindFetchLocalSoundsForPathUseCase(
        fetchLocalSoundsForPathUseCase: ProdFetchLocalSoundsForPathUseCase
    ): FetchLocalSoundsForPathUseCase

    @Binds
    abstract fun bindFetchSoundByteArrayForPathUseCase(
        fetchSoundByteArrayForPathUseCase: ProdFetchSoundByteArrayForPathUseCase,
    ): FetchSoundByteArrayForPathUseCase

    @Binds
    abstract fun bindFetchSoundsForPathUseCase(
        fetchSoundsForPathUseCase: ProdFetchSoundsForPathUseCase,
    ): FetchSoundsForPathUseCase

    @Binds
    abstract fun bindGetFileExistsUseCase(
        getFileExistsUseCase: ProdGetFileExistsUseCase
    ): GetFileExistsUseCase

    @Binds
    abstract fun bindSaveSoundLocallyUseCase(
        saveSoundLocallyUseCase: ProdSaveSoundLocallyUseCase
    ): SaveSoundLocallyUseCase

    @Binds
    abstract fun bindShareSoundUseCase(
        shareSoundUseCase: ProdShareSoundUseCase
    ): ShareSoundUseCase

    // ------------- CATEGORIES ----------------

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
