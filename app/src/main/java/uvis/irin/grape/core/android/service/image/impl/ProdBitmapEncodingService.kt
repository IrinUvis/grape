package uvis.irin.grape.core.android.service.image.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uvis.irin.grape.core.android.service.image.BitmapEncodingService

class ProdBitmapEncodingService @Inject constructor(
    @ApplicationContext private val context: Context,
) : BitmapEncodingService {

    override fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    override fun drawableToBitmap(drawable: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, drawable)
    }
}
