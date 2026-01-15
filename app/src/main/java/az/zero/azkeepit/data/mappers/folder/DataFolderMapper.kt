package az.zero.azkeepit.data.mappers.folder

import az.zero.azkeepit.data.local.entities.folder.DbFolder
import az.zero.azkeepit.domain.commons.INVALID_ID
import az.zero.azkeepit.domain.models.folder.DomainFolder


fun DbFolder.toDomainFolder() = DomainFolder(
    folderId = this.folderId?: INVALID_ID,
    name = this.name,
    createdAt = this.createdAt,
)

fun DomainFolder.toDbFolder() = DbFolder(
    folderId = this.folderId,
    name = this.name,
    createdAt = this.createdAt,
)