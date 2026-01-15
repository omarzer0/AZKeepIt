package az.zero.azkeepit.domain.models.folder

import az.zero.azkeepit.domain.models.note.DomainNote

data class DomainFolderWithNotes(
    val folderId: Long,
    val name: String,
    val createdAt: Long,
    val notes : List<DomainNote>
)