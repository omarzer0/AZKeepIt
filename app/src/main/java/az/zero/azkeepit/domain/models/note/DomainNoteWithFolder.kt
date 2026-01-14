package az.zero.azkeepit.domain.models.note

import az.zero.azkeepit.domain.models.folder.DomainFolder

data class DomainNoteWithFolder(
    val note: DomainNote,
    val folder: DomainFolder?
)