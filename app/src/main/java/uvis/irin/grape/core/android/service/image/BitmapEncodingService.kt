package uvis.irin.grape.core.android.service.image

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

interface BitmapEncodingService {

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap

    fun drawableToBitmap(@DrawableRes drawable: Int): Bitmap
}
