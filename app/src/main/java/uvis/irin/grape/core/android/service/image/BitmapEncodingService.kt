package uvis.irin.grape.core.android.service.image

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

interface BitmapEncodingService {

    suspend fun byteArrayToBitmap(byteArray: ByteArray): Bitmap

    suspend fun drawableToBitmap(@DrawableRes drawable: Int): Bitmap
}
