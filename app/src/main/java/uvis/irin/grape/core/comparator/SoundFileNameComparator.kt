package uvis.irin.grape.core.comparator

import java.text.Collator
import java.util.Locale

class SoundFileNameComparator : Comparator<String> {

    override fun compare(soundFileName1: String?, soundFileName2: String?): Int {
        return when {
            (soundFileName1 == null && soundFileName2 == null) -> 0
            (soundFileName1 == null) -> -1
            (soundFileName2 == null) -> 1
            else -> {
                Collator.getInstance(Locale("pl", "PL"))
                    .compare(soundFileName1, soundFileName2)
            }
        }
    }
}
