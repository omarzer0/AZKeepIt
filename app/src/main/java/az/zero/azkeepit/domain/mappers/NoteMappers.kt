package az.zero.azkeepit.domain.mappers

import az.zero.azkeepit.data.local.entities.*

data class UiNote(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val noteId: Long,
    val isSelected: Boolean,
    val ownerUiFolder: UiFolder?,
)

fun Note.toUiNote(
    isSelected: Boolean = false,
    ownerUiFolder: UiFolder? = null,
) = UiNote(
    noteId = this.noteId!!,
    title = this.title,
    content = this.content,
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    isSelected = isSelected,
    ownerUiFolder = ownerUiFolder
)

fun NoteWithFolder.toUiNote(isSelected: Boolean = false) = UiNote(
    noteId = this.note.noteId!!,
    title = this.note.title,
    content = this.note.content,
    isLocked = this.note.isLocked,
    createdAt = this.note.createdAt,
    isSelected = isSelected,
    ownerUiFolder = this.folder?.toUiFolder()
)

fun List<NoteWithFolder>.toUiNotes() = this.map {
    it.toUiNote()
}