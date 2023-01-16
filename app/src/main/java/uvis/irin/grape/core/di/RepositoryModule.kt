package uvis.irin.grape.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uvis.irin.grape.soundlist.data.repository.SoundRepository
import uvis.irin.grape.soundlist.data.repository.impl.FirebaseSoundRepository
import uvis.irin.grape.soundlist.domain.repository.ProdSoundListRepository
import uvis.irin.grape.soundlist.domain.repository.SoundListRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSoundListRepository(
        soundListRepository: ProdSoundListRepository
    ): SoundListRepository

    @Binds
    abstract fun bindSoundRepository(
        soundRepository: FirebaseSoundRepository,
    ): SoundRepository
}
