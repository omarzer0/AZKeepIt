package az.zero.azkeepit.domain.mappers

import az.zero.azkeepit.data.local.entities.DbFolder
import az.zero.azkeepit.data.local.entities.DbFolderWithNotes


data class UiFolder(
    val name: String,
    val createdAt: Long,
    val folderId: Long,
    val folderNotes: List<UiNote> = emptyList(),
    val isSelected: Boolean = false,
)

fun DbFolderWithNotes.toUiFolder() = UiFolder(
    name = this.dbFolder.name,
    createdAt = this.dbFolder.createdAt,
    folderId = this.dbFolder.folderId!!,
    folderNotes = this.dbNotes.map {
        it.toUiNote(ownerUiFolder = this.dbFolder.toUiFolder())
    },
    isSelected = false
)


fun DbFolder.toUiFolder() = UiFolder(
    name = this.name,
    createdAt = this.createdAt,
    folderId = this.folderId!!,
    isSelected = false
)

fun List<DbFolderWithNotes>.toUiFolders() = this.map {
    it.toUiFolder()
}