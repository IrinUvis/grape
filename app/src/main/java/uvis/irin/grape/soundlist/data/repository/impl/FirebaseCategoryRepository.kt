package uvis.irin.grape.soundlist.data.repository.impl

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.soundlist.data.model.Category
import uvis.irin.grape.soundlist.data.repository.CategoryRepository

class FirebaseCategoryRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
) : CategoryRepository {
    companion object {
        private const val TAG = "FirebaseCategoryRepository"
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
}

fun StorageReference.toCategory() = Category(
    name = this.name,
    absolutePath = this.path
)
