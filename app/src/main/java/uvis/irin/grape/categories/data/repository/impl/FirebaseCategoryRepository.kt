package uvis.irin.grape.categories.data.repository.impl

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.categories.data.model.Category
import uvis.irin.grape.categories.data.repository.CategoryRepository
import uvis.irin.grape.core.constants.MAX_FIREBASE_FILE_SIZE_IN_BYTES
import uvis.irin.grape.core.di.IoDispatcher

class FirebaseCategoryRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val firebaseStorage: FirebaseStorage,
) : CategoryRepository {
    companion object {
        private const val TAG = "FirebaseCategoryRepository"
        private const val CATEGORY_IMAGE_FILE_NAME = "00000.jpg"
    }

    override suspend fun fetchCategoriesForPath(path: String): DataResult<List<Category>> {
        return withContext(ioDispatcher) {
            try {
                val reference = firebaseStorage.reference.child(path)
                val result = reference.listAll().await()
                val categoryStorageReferences = result.prefixes
                val categories = categoryStorageReferences
                    .map { storageReference -> async { isFinalCategory(storageReference) } }
                    .awaitAll()
                    .zip(categoryStorageReferences)
                    .map { pair ->
                        val isFinalCategory = pair.first
                        val storageReference = pair.second

                        storageReference.toCategory(
                            isFinalCategory = isFinalCategory,
                        )
                    }

                DataResult.Success(categories)
            } catch (e: StorageException) {
                Log.d(TAG, "fetchCategoriesForPath: e")
                DataResult.Failure(e)
            }
        }
    }

    override suspend fun fetchImageByteArrayForPath(path: String): DataResult<ByteArray> {
        return withContext(ioDispatcher) {
            try {
                val reference =
                    firebaseStorage.reference.child(path).child(CATEGORY_IMAGE_FILE_NAME)
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

    private suspend fun isFinalCategory(reference: StorageReference): Boolean {
        val prefixes = reference.listAll().await().prefixes
        return prefixes.isEmpty() // no more subdirs
    }
}

fun StorageReference.toCategory(isFinalCategory: Boolean) = Category(
    path = this.path,
    isFinalCategory = isFinalCategory,
)
