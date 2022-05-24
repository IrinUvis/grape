package uvis.irin.grape.soundlist.domain.repository

import android.content.Context
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory

interface SoundListRepository {

    suspend fun fetchAllCategories(): Result<List<SoundCategory>>

    fun fetchSoundsByCategory(category: SoundCategory, context: Context): Result<List<Sound>>
}
