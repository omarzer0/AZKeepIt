package az.zero.azkeepit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Folder(
    val name: String,
    val createdAt: Long,
    @PrimaryKey(autoGenerate = true)
    val folderId: Long? = null,
)

data class UiFolder(
    val name: String,
    val createdAt: Long,
    val folderId: Long,
    val folderNotes: List<UiNote> = emptyList(),
    val isSelected: Boolean = false,
)

fun Folder.toUiFolder(isSelected: Boolean = false) = UiFolder(
    name = this.name,
    createdAt = this.createdAt,
    folderId = this.folderId!!,
    isSelected = isSelected
)

fun List<Folder>.toUiFolders() = this.map {
    it.toUiFolder()
}