package uvis.irin.grape.soundlist.data.repository.impl

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import uvis.irin.grape.core.constants.MAX_FIREBASE_FILE_SIZE_IN_BYTES
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.soundlist.data.model.Sound
import uvis.irin.grape.soundlist.data.repository.SoundRepository

class FirebaseSoundRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
) : SoundRepository {
    companion object {
        private const val TAG = "FirebaseSoundRepository"
    }

    override suspend fun fetchSoundsForPath(path: String): DataResult<List<Sound>> {
        return try {
            val reference = firebaseStorage.reference.child(path)
            val result = reference.listAll().await()
            DataResult.Success(
                result.items.map { it.toSound() }
            )
        } catch (e: StorageException) {
            Log.d(TAG, "fetchSoundsForPath: $e")
            DataResult.Failure(e)
        }
    }

    override suspend fun fetchByteArrayForPath(path: String): DataResult<ByteArray> {
        return try {
            val reference = firebaseStorage.reference.child(path)
            val result = reference.getBytes(MAX_FIREBASE_FILE_SIZE_IN_BYTES).await()
            DataResult.Success(result)
        } catch (e: StorageException) {
            Log.d(TAG, "fetchSoundsForPath: $e")
            DataResult.Failure(e)
        } catch (e: IndexOutOfBoundsException) {
            Log.d(TAG, "fetchSoundsForPath: $e")
            DataResult.Failure(e)
        }
    }
}

fun StorageReference.toSound() = Sound(
    name = this.name,
    path = this.path,
)
