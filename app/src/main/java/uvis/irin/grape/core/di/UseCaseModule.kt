package uvis.irin.grape.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}
