package uvis.irin.grape.soundlist.domain.model

import java.util.UUID

abstract class Sound {
    abstract val id: UUID
    abstract val name: String
}