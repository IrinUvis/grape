package uvis.irin.grape.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uvis.irin.grape.categories.data.repository.CategoryRepository
import uvis.irin.grape.categories.data.repository.impl.FirebaseCategoryRepository
import uvis.irin.grape.soundlist.data.repository.FavouriteSoundRepository
import uvis.irin.grape.soundlist.data.repository.SoundRepository
import uvis.irin.grape.soundlist.data.repository.impl.FirebaseSoundRepository
import uvis.irin.grape.soundlist.data.repository.impl.RoomFavouriteSoundRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSoundRepository(
        soundRepository: FirebaseSoundRepository,
    ): SoundRepository

    @Binds
    abstract fun bindFavouriteSoundRepository(
        favouriteSoundRepository: RoomFavouriteSoundRepository
    ): FavouriteSoundRepository

    @Binds
    abstract fun bindCategoryRepository(
        categoryRepository: FirebaseCategoryRepository,
    ): CategoryRepository
}
