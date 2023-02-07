package uvis.irin.grape.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uvis.irin.grape.core.data.local.db.GrapeDatabase
import uvis.irin.grape.core.data.local.favouritesounds.FavouriteSoundDao

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val DATABASE_NAME = "grape2-database.db"

    @Provides
    fun provideGrapeDatabase(
        @ApplicationContext context: Context,
    ): GrapeDatabase {
        return Room.databaseBuilder(
            context,
            GrapeDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideFavouriteSoundDao(
        database: GrapeDatabase
    ): FavouriteSoundDao {
        return database.favouriteSoundDao()
    }
}
