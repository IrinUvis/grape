package uvis.irin.grape.core.extension

import java.util.Locale

fun String.capitalize(): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

fun String.withForwardSlashesReplacedByDashes(): String {
    return replace('/', '-')
}

fun String.withDashesReplacedByForwardSlashes(): String {
    return replace('-', '/')
}
