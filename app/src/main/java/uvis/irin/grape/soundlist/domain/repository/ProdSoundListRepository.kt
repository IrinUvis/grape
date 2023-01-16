package uvis.irin.grape.soundlist.domain.repository

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import uvis.irin.grape.core.capitalize
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.core.data.local.favouritesounds.FavouriteSoundDao
import uvis.irin.grape.core.data.local.favouritesounds.toPersistableFavouriteSound
import uvis.irin.grape.core.data.local.favouritesounds.toResourceSound
import uvis.irin.grape.core.trimFileExtension
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory
import java.io.IOException
import javax.inject.Inject

class ProdSoundListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val favouriteSoundDao: FavouriteSoundDao
) : SoundListRepository {

    companion object {
        const val ASSET_SOUNDS_PATH = "sounds"
    }

    override suspend fun fetchAllCategories(): DataResult<List<ResourceSoundCategory>> {
        val assetManager = context.assets

        return assetManager.list(ASSET_SOUNDS_PATH)?.let { directoriesNames ->
            DataResult.Success(
                data = directoriesNames.map { directoryName ->
                    val subcategories = findSubcategoriesForCategory(
                        categoryPath = "$ASSET_SOUNDS_PATH/$directoryName",
                        assetManager = assetManager
                    )

                    ResourceSoundCategory(
                        name = directoryName.split('_').last().capitalize(),
                        subcategories = subcategories,
                        assetsPath = "$ASSET_SOUNDS_PATH/$directoryName"
                    )
                },
            )
        } ?: DataResult.Failure(failure = IOException("An error has occurred while reading the assets"))
    }

    override suspend fun fetchSoundsByCategory(category: ResourceSoundCategory): DataResult<List<ResourceSound>> {
        val assetManager = context.assets

        return assetManager.list(category.assetsPath)
            ?.filter { it.endsWith(".mp3") }
            ?.let { soundFilenames ->
                DataResult.Success(
                    data = soundFilenames.map {
                        ResourceSound(
                            name = it
                                .split('_').last().capitalize().trimFileExtension(),
                            category = category,
                            relativeAssetPath = it
                        )
                    }
                )
            }
            ?: DataResult.Failure(failure = IOException("An error has occurred while reading the assets"))
    }

    override suspend fun fetchAllFavouriteSounds(): DataResult<List<ResourceSound>> =
        favouriteSoundDao.getAll().map { it.toResourceSound() }.let { sounds ->
            val assetManager = context.assets

            val filteredSounds = sounds.toMutableList()

            for (sound in sounds) {
                try {
                    assetManager.open(sound.completePath)
                } catch (ex: IOException) {
                    Log.i(
                        "FAVOURITE SOUND",
                        "Favourite sound with path ${sound.completePath} can't be found at this path. " +
                            "Therefore it has been removed from favourite_sound table in the database.",
                        ex
                    )
                    deleteFavouriteSound(sound)
                    filteredSounds.remove(sound)
                }
            }

            DataResult.Success(data = filteredSounds)
        }

    override suspend fun insertFavouriteSound(favouriteSound: ResourceSound) {
        favouriteSoundDao.insertFavouriteSound(favouriteSound.toPersistableFavouriteSound())
    }

    override suspend fun deleteFavouriteSound(favouriteSound: ResourceSound) {
        favouriteSoundDao.deleteFavouriteSound(favouriteSound.toPersistableFavouriteSound())
    }

    private fun findSubcategoriesForCategory(
        categoryPath: String,
        assetManager: AssetManager = context.assets
    ): List<ResourceSoundCategory>? {
        return assetManager.list(categoryPath)
            ?.filter { !it.endsWith(".mp3") }
            .takeIf {
                it.orEmpty().isNotEmpty()
            }
            ?.let { subcategoriesNames ->
                subcategoriesNames.map {
                    ResourceSoundCategory(
                        name = it.split('_').last().capitalize(),
                        subcategories = null,
                        assetsPath = "$categoryPath/$it"
                    )
                }
            }
    }
}
