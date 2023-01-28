package az.zero.azkeepit.domain.mappers

import android.net.Uri
import androidx.compose.ui.graphics.Color
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.data.local.entities.NoteWithFolder
import az.zero.azkeepit.util.JDateTimeUtil

data class UiNote(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val shortDateTime: String,
    val longDateTime: String,
    val images: List<Uri>,
    val color: Color,
    val password: String? = null,
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
    images = this.images.map { Uri.parse(it) },
    color = Color(this.color),
    password = password,
    shortDateTime = JDateTimeUtil.toShortDateTimeFormat(this.createdAt),
    longDateTime = JDateTimeUtil.toLongDateTimeFormat(this.createdAt),
    isSelected = isSelected,
    ownerUiFolder = ownerUiFolder
)

fun NoteWithFolder.toUiNote(isSelected: Boolean = false) = UiNote(
    noteId = this.note.noteId!!,
    title = this.note.title,
    content = this.note.content,
    isLocked = this.note.isLocked,
    createdAt = this.note.createdAt,
    images = this.note.images.map { Uri.parse(it) },
    color = Color(this.note.color),
    password = null,
    shortDateTime = JDateTimeUtil.toShortDateTimeFormat(this.note.createdAt),
    longDateTime = JDateTimeUtil.toLongDateTimeFormat(this.note.createdAt),
    isSelected = isSelected,
    ownerUiFolder = this.folder?.toUiFolder()
)

fun List<NoteWithFolder>.toUiNotes() = this.map {
    it.toUiNote()
}