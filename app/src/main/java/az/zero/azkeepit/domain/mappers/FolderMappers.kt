package az.zero.azkeepit.domain.mappers

import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.FolderWithNotes


data class UiFolder(
    val name: String,
    val createdAt: Long,
    val folderId: Long,
    val folderNotes: List<UiNote> = emptyList(),
    val isSelected: Boolean = false,
)

fun FolderWithNotes.toUiFolder() = UiFolder(
    name = this.folder.name,
    createdAt = this.folder.createdAt,
    folderId = this.folder.folderId!!,
    folderNotes = this.notes.map {
        it.toUiNote(ownerUiFolder = this.folder.toUiFolder())
    },
    isSelected = false
)


fun Folder.toUiFolder() = UiFolder(
    name = this.name,
    createdAt = this.createdAt,
    folderId = this.folderId!!,
    isSelected = false
)

fun List<FolderWithNotes>.toUiFolders() = this.map {
    it.toUiFolder()
}