package uvis.irin.grape.core.provider.file.impl

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import uvis.irin.grape.BuildConfig
import uvis.irin.grape.core.provider.file.FileSharingService

class ProdFileSharingService @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileSharingService {

    override fun shareFile(
        file: File,
        mimeType: String,
    ) {
        val authority = BuildConfig.APPLICATION_ID + ".fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, file)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = mimeType
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        val chooserIntent = Intent.createChooser(shareIntent, null).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.queryIntentActivities(
                chooserIntent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )
        } else {
            context.packageManager.queryIntentActivities(
                chooserIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        }

        for (resolveInfo in resolveInfos) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(chooserIntent)
    }
}