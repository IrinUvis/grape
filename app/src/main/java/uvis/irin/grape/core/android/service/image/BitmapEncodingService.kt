package uvis.irin.grape.core.android.service.image

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

interface BitmapEncodingService {

    suspend fun byteArrayToBitmap(byteArray: ByteArray): Bitmap

    suspend fun pngDrawableToBitmap(@DrawableRes drawable: Int): Bitmap

    suspend fun xmlDrawableToBitmap(@DrawableRes drawable: Int): Bitmap
}
