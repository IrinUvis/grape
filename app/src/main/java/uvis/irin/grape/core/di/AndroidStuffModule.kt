package uvis.irin.grape.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uvis.irin.grape.core.provider.file.FileSharingService
import uvis.irin.grape.core.provider.file.impl.ProdFileSharingService

@Module
@InstallIn(SingletonComponent::class)
abstract class AndroidStuffModule {

    @Binds
    abstract fun bindFileSharingService(
        fileSharingService: ProdFileSharingService
    ): FileSharingService
}