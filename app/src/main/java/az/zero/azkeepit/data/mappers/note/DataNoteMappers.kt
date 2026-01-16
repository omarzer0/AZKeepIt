package az.zero.azkeepit.data.mappers.note

import az.zero.azkeepit.data.local.entities.folder.DbFolder
import az.zero.azkeepit.data.local.entities.note.DbNote
import az.zero.azkeepit.data.local.entities.note.DbNoteWithFolder
import az.zero.azkeepit.data.mappers.folder.toDbFolder
import az.zero.azkeepit.data.mappers.folder.toDomainFolder
import az.zero.azkeepit.domain.commons.INVALID_ID
import az.zero.azkeepit.domain.models.note.DomainNote

fun DbNote.toDomainNote(ownerDbFolder: DbFolder) = DomainNote(
    noteId = this.noteId ?: INVALID_ID,
    title = this.title,
    content = this.content,
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    images = this.images,
    colorArgb = this.color,
    hashedPassword = this.hashedPassword,
    ownerDomainFolder = ownerDbFolder.toDomainFolder(),
)

fun DomainNote.toDbNote() = DbNote(
    noteId = this.noteId,
    title = this.title,
    content = this.content,
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    images = this.images,
    color = this.colorArgb,
    hashedPassword = this.hashedPassword,
    ownerFolderId = this.ownerDomainFolder?.folderId,
)


fun DbNoteWithFolder.toDomainNote() = DomainNote(
    noteId = this.dbNote.noteId ?: INVALID_ID,
    title = this.dbNote.title,
    content = this.dbNote.content,
    isLocked = this.dbNote.isLocked,
    createdAt = this.dbNote.createdAt,
    images = this.dbNote.images,
    colorArgb = this.dbNote.color,
    hashedPassword = this.dbNote.hashedPassword,
    ownerDomainFolder = this.dbFolder?.toDomainFolder()
)

fun DomainNote.toDbNoteWithFolder() = DbNoteWithFolder(
    dbNote = DbNote(
        noteId = this.noteId,
        title = this.title,
        content = this.content,
        isLocked = this.isLocked,
        createdAt = this.createdAt,
        images = this.images,
        color = this.colorArgb,
        hashedPassword = this.hashedPassword,
        ownerFolderId = this.ownerDomainFolder?.folderId
    ),
    dbFolder = ownerDomainFolder?.toDbFolder(),
)