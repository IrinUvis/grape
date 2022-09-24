package uvis.irin.grape.core

import java.util.Locale

fun String.capitalize() = replaceFirstChar { char ->
    if (char.isLowerCase()) char.titlecase(
        Locale.getDefault()
    ) else char.toString()
}

fun String.trimFileExtension() = replace("\\.\\w+$".toRegex(), "")
