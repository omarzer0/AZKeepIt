package az.zero.azkeepit.domain.models.folder

import az.zero.azkeepit.domain.models.note.DomainNote

data class DomainFolderWithNotes(
    val folder: DomainFolder,
    val notes: List<DomainNote>
)