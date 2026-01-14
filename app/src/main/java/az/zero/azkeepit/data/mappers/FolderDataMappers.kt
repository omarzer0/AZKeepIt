package az.zero.azkeepit.data.mappers

import az.zero.azkeepit.data.local.entities.folder.DbFolder
import az.zero.azkeepit.data.local.entities.folder.DbFolderWithNotes
import az.zero.azkeepit.domain.models.folder.DomainFolder
import az.zero.azkeepit.domain.models.folder.DomainFolderWithNotes

// DbFolder → DomainFolder
fun DbFolder.toDomain() = DomainFolder(
    folderId = this.folderId ?: 0L,
    name = this.name,
    createdAt = this.createdAt
)

// DomainFolder → DbFolder
fun DomainFolder.toDb() = DbFolder(
    folderId = if (this.folderId == 0L) null else this.folderId,
    name = this.name.trim(),
    createdAt = this.createdAt
)

// DbFolderWithNotes → DomainFolderWithNotes
fun DbFolderWithNotes.toDomain() = DomainFolderWithNotes(
    folder = this.dbFolder.toDomain(),
    notes = this.dbNotes.map { it.toDomain() }
)

// List extensions
fun List<DbFolder>.toDomain() = map { it.toDomain() }
fun List<DbFolderWithNotes>.toDomainFolders() = map { it.toDomain() }