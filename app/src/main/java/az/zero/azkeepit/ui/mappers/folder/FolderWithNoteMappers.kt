package az.zero.azkeepit.ui.mappers.folder

import az.zero.azkeepit.domain.models.folder.DomainFolderWithNotes
import az.zero.azkeepit.ui.mappers.note.toUiNote
import az.zero.azkeepit.ui.models.folder.UiFolderWithNotes

fun DomainFolderWithNotes.toUiFolderWithNote() = UiFolderWithNotes(
    folderId = this.folderId,
    name = this.name,
    createdAt = this.createdAt,
    folderNotes = this.notes.map { it.toUiNote() }
)