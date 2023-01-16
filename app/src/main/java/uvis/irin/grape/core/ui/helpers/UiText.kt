package uvis.irin.grape.core.ui.helpers

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UiText {
    data class StringText(val value: String) : UiText()

    data class ResourceText(@StringRes val value: Int) : UiText()
}

fun UiText.getString(context: Context) = when (this) {
    is UiText.StringText -> this.value
    is UiText.ResourceText -> context.getString(this.value)
}

@Composable
fun UiText.getString() = getString(LocalContext.current)
