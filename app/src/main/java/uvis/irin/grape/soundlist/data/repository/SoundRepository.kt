package uvis.irin.grape.soundlist.data.repository

import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.soundlist.data.model.Sound

interface SoundRepository {

    suspend fun fetchSoundsForPath(path: String): DataResult<List<Sound>>

    suspend fun fetchByteArrayForPath(path: String): DataResult<ByteArray>
}
