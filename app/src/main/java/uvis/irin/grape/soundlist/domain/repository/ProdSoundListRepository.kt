package uvis.irin.grape.soundlist.domain.repository

import android.content.Context
import android.content.res.AssetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import uvis.irin.grape.core.capitalize
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.core.data.local.favouritesounds.FavouriteSoundDao
import uvis.irin.grape.core.data.local.favouritesounds.toPersistableFavouriteSound
import uvis.irin.grape.core.trimFileExtension
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory
import java.io.IOException
import javax.inject.Inject
import uvis.irin.grape.core.data.local.favouritesounds.toResourceSound

class ProdSoundListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val favouriteSoundDao: FavouriteSoundDao
) : SoundListRepository {

    companion object {
        const val ASSET_SOUNDS_PATH = "sounds"
    }

    override suspend fun fetchAllCategories(): Result<List<ResourceSoundCategory>> {
        val assetManager = context.assets

        return assetManager.list(ASSET_SOUNDS_PATH)?.let { directoriesNames ->
            Result.Success(
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
        } ?: Result.Error(error = IOException("An error has occurred while reading the assets"))
    }

    override suspend fun fetchSoundsByCategory(category: ResourceSoundCategory): Result<List<ResourceSound>> {
        val assetManager = context.assets

        return assetManager.list(category.assetsPath)
            ?.filter { it.endsWith(".mp3") }
            ?.let { soundFilenames ->
                Result.Success(
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
            ?: Result.Error(error = IOException("An error has occurred while reading the assets"))
    }

    override suspend fun fetchAllFavouriteSounds(): Result<List<ResourceSound>> =
        favouriteSoundDao.getAll().map { it.toResourceSound() }.let {
            Result.Success(data = it)
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
