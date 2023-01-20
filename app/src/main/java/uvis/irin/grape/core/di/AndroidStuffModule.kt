package uvis.irin.grape.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.core.android.service.file.FileSharingService
import uvis.irin.grape.core.android.service.file.FileWritingService
import uvis.irin.grape.core.android.service.file.impl.ProdFileReadingService
import uvis.irin.grape.core.android.service.file.impl.ProdFileSharingService
import uvis.irin.grape.core.android.service.file.impl.ProdFileWritingService

@Module
@InstallIn(SingletonComponent::class)
abstract class AndroidStuffModule {

    @Binds
    abstract fun bindFileSharingService(
        fileSharingService: ProdFileSharingService
    ): FileSharingService

    @Binds
    abstract fun bindFileWritingService(
        fileWritingService: ProdFileWritingService
    ): FileWritingService

    @Binds
    abstract fun bindFileReadingService(
        fileReadingService: ProdFileReadingService
    ): FileReadingService
}
