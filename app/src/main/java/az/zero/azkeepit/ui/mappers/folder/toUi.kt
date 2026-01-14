package az.zero.azkeepit.ui.mappers.folder

import az.zero.azkeepit.domain.models.folder.DomainFolder
import az.zero.azkeepit.domain.models.folder.DomainFolderWithNotes
import az.zero.azkeepit.ui.mappers.note.toUi
import az.zero.azkeepit.ui.models.folder.UiFolder
import az.zero.azkeepit.ui.models.folder.UiFolderWithNotes

// DomainFolder → UiFolder
fun DomainFolder.toUi(isSelected: Boolean = false) = UiFolder(
    folderId = this.folderId,
    name = this.name,
    createdAt = this.createdAt,
    isSelected = isSelected
)

// UiFolder → DomainFolder
fun UiFolder.toDomain() = DomainFolder(
    folderId = this.folderId,
    name = this.name.trim(),
    createdAt = this.createdAt
)

// DomainFolderWithNotes → UiFolderWithNotes
fun DomainFolderWithNotes.toUi(isSelected: Boolean = false) = UiFolderWithNotes(
    folder = this.folder.toUi(isSelected = isSelected),
    notes = this.notes.map { it.toUi(ownerUiFolder = this.folder.toUi()) }
)

// List extensions
fun List<DomainFolder>.toUi() = map { it.toUi() }
fun List<DomainFolderWithNotes>.toUiFolders() = map { it.toUi() }