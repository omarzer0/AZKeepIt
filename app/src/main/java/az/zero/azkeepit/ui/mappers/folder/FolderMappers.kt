package az.zero.azkeepit.ui.mappers.folder

import az.zero.azkeepit.domain.models.folder.DomainFolder
import az.zero.azkeepit.ui.models.folder.UiFolder


fun DomainFolder.toUiFolder(isSelected: Boolean = false) = UiFolder(
    folderId = this.folderId,
    name = this.name,
    createdAt = this.createdAt,
    isSelected = isSelected,
)


fun UiFolder.toDomainFolder() = DomainFolder(
    folderId = this.folderId,
    name = this.name,
    createdAt = this.createdAt,
)