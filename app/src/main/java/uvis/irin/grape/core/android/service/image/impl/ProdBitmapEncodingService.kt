package uvis.irin.grape.core.android.service.image.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uvis.irin.grape.core.android.service.image.BitmapEncodingService
import uvis.irin.grape.core.di.DefaultDispatcher

class ProdBitmapEncodingService @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : BitmapEncodingService {

    override suspend fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return withContext(defaultDispatcher) {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    override suspend fun drawableToBitmap(drawable: Int): Bitmap {
        return withContext(defaultDispatcher) {
            BitmapFactory.decodeResource(context.resources, drawable)
        }
    }
}
