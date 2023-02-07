package uvis.irin.grape.core.comparator

import java.text.Collator
import java.util.Locale

class DirectoryAndFileNameComparator : Comparator<String> {

    override fun compare(name1: String?, name2: String?): Int {
        return when {
            (name1 == null && name2 == null) -> 0
            (name1 == null) -> -1
            (name2 == null) -> 1
            else -> {
                Collator.getInstance(Locale("pl", "PL"))
                    .compare(name1, name2)
            }
        }
    }
}
