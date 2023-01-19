package az.zero.azkeepit.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Note(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val ownerFolderId: Long?,
    @PrimaryKey(autoGenerate = true)
    val noteId: Long? = null,
)


data class NoteWithFolder(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "ownerFolderId",
        entityColumn = "folderId"
    )
    val folder: Folder?,
)

data class UiNote(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val ownerFolderId: Long?,
    val ownerFolderName: String,
    val noteId: Long,
    val isSelected: Boolean,
)

fun Note.toUiNote(isSelected: Boolean = false) = UiNote(
    noteId = this.noteId!!,
    title = this.title,
    content = this.content,
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    ownerFolderId = this.ownerFolderId,
    ownerFolderName = "",
    isSelected = isSelected
)

fun NoteWithFolder.toUiNote(isSelected: Boolean = false) = UiNote(
    noteId = this.note.noteId!!,
    title = this.note.title,
    content = this.note.content,
    isLocked = this.note.isLocked,
    createdAt = this.note.createdAt,
    ownerFolderId = this.note.ownerFolderId,
    ownerFolderName = this.folder?.name ?: "",
    isSelected = isSelected
)

fun List<NoteWithFolder>.toUiNotes() = this.map {
    it.toUiNote()
}