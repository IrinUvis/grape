package uvis.irin.grape.soundlist.domain.model

import java.util.UUID

data class ResourceSound(
    override val id: UUID,
    override val name: String,
    override val category: SoundCategory,
    val resId: Int,
) : Sound
