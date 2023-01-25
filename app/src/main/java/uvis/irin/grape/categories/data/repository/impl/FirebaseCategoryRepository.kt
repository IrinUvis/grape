package uvis.irin.grape.categories.data.repository.impl

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.categories.data.model.Category
import uvis.irin.grape.categories.data.repository.CategoryRepository
import uvis.irin.grape.core.constants.MAX_FIREBASE_FILE_SIZE_IN_BYTES

class FirebaseCategoryRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
) : CategoryRepository {
    companion object {
        private const val TAG = "FirebaseCategoryRepository"
        private const val CATEGORY_IMAGE_FILE_NAME = "00000.jpg"
    }

    override suspend fun fetchCategoriesForPath(path: String): DataResult<List<Category>> {
        return try {
            val reference = firebaseStorage.reference.child(path)
            val result = reference.listAll().await()
            DataResult.Success(
                result.prefixes.map { it.toCategory() }
            )
        } catch (e: StorageException) {
            Log.d(TAG, "fetchCategoriesForPath: e")
            DataResult.Failure(e)
        }
    }

    override suspend fun fetchImageByteArrayForPath(path: String): DataResult<ByteArray> {
        return try {
            val reference = firebaseStorage.reference.child(path).child(CATEGORY_IMAGE_FILE_NAME)
            val result = reference.getBytes(MAX_FIREBASE_FILE_SIZE_IN_BYTES).await()
            DataResult.Success(result)
        } catch (e: StorageException) {
            Log.d(TAG, "fetchImageByteArrayForPath: $e")
            DataResult.Failure(e)
        } catch (e: IndexOutOfBoundsException) {
            Log.d(TAG, "fetchImageByteArrayForPath: $e")
            DataResult.Failure(e)
        }
    }
}

fun StorageReference.toCategory() = Category(
    name = this.name,
    path = this.path
)
