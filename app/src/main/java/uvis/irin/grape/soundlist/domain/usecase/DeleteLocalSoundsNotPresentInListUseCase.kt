package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.soundlist.domain.model.DomainSound

interface DeleteLocalSoundsNotPresentInListUseCase {

    suspend operator fun invoke(path: String, soundList: List<DomainSound>)
}
