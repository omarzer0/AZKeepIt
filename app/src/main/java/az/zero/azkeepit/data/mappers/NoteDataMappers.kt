package az.zero.azkeepit.data.mappers

import az.zero.azkeepit.data.local.entities.note.DbNote
import az.zero.azkeepit.data.local.entities.note.DbNoteWithFolder
import az.zero.azkeepit.domain.models.note.DomainNote
import az.zero.azkeepit.domain.models.note.DomainNoteWithFolder

// DbNote → DomainNote
fun DbNote.toDomain() = DomainNote(
    noteId = this.noteId ?: 0L,
    title = this.title,
    content = this.content,
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    ownerFolderId = this.ownerFolderId,
    images = this.images,
    colorArgb = this.color,
    hashedPassword = this.hashedPassword
)

// DomainNote → DbNote
fun DomainNote.toDb() = DbNote(
    noteId = if (this.noteId == 0L) null else this.noteId,
    title = this.title.trim(),
    content = this.content.trim(),
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    ownerFolderId = this.ownerFolderId,
    images = this.images,
    color = this.colorArgb,
    hashedPassword = this.hashedPassword
)

// DbNoteWithFolder → DomainNoteWithFolder
fun DbNoteWithFolder.toDomain() = DomainNoteWithFolder(
    note = this.dbNote.toDomain(),
    folder = this.dbFolder?.toDomain()
)

// List extensions
fun List<DbNote>.toDomain() = map { it.toDomain() }
fun List<DbNoteWithFolder>.toDomainNotes() = map { it.toDomain() }