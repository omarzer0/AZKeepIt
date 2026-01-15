package az.zero.azkeepit.util

import az.zero.azkeepit.ui.models.folder.UiFolder
import az.zero.azkeepit.ui.models.folder.UiFolderWithNotes


private const val folderInitialName = ""
val emptyUiFolder = UiFolder(
    name = folderInitialName,
    createdAt = -1L,
    folderId = -1L,
    isSelected = false
)

val emptyUiFolderWithNotes = UiFolderWithNotes(
    name = folderInitialName,
    createdAt = -1L,
    folderId = -1L,
    folderNotes = emptyList()
)