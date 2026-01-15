package az.zero.azkeepit.ui.models.folder

import az.zero.azkeepit.ui.models.note.UiNote


data class UiFolderWithNotes(
    val folderId: Long,
    val name: String,
    val createdAt: Long,
    val folderNotes: List<UiNote>,
)