package uvis.irin.grape.soundlist.domain.model

interface SoundCategory {
    val name: String
    val subcategories: List<SoundCategory>?
}
