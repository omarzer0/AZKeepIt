package az.zero.azkeepit.data.mappers.folder

import az.zero.azkeepit.data.local.entities.folder.DbFolderWithNotes
import az.zero.azkeepit.data.mappers.note.toDomainNote
import az.zero.azkeepit.domain.commons.INVALID_ID
import az.zero.azkeepit.domain.models.folder.DomainFolderWithNotes


fun DbFolderWithNotes.toDomainFolderWithNotes() = DomainFolderWithNotes(
    folderId = this.dbFolder.folderId ?: INVALID_ID,
    name = this.dbFolder.name,
    createdAt = this.dbFolder.createdAt,
    notes = this.dbNotes.map { it.toDomainNote(this.dbFolder) }
)