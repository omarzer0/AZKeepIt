package az.zero.azkeepit.domain.mappers

import android.net.Uri
import androidx.compose.ui.graphics.Color
import az.zero.azkeepit.data.local.entities.note.DbNote
import az.zero.azkeepit.data.local.entities.note.DbNoteWithFolder
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

fun DbNote.toUiNote(
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
    password = hashedPassword,
    shortDateTime = JDateTimeUtil.toShortDateTimeFormat(this.createdAt),
    longDateTime = JDateTimeUtil.toLongDateTimeFormat(this.createdAt),
    isSelected = isSelected,
    ownerUiFolder = ownerUiFolder
)

fun DbNoteWithFolder.toUiNote(isSelected: Boolean = false) = UiNote(
    noteId = this.dbNote.noteId!!,
    title = this.dbNote.title,
    content = this.dbNote.content,
    isLocked = this.dbNote.isLocked,
    createdAt = this.dbNote.createdAt,
    images = this.dbNote.images.map { Uri.parse(it) },
    color = Color(this.dbNote.color),
    password = this.dbNote.hashedPassword,
    shortDateTime = JDateTimeUtil.toShortDateTimeFormat(this.dbNote.createdAt),
    longDateTime = JDateTimeUtil.toLongDateTimeFormat(this.dbNote.createdAt),
    isSelected = isSelected,
    ownerUiFolder = this.dbFolder?.toUiFolder()
)

fun List<DbNoteWithFolder>.toUiNotes() = this.map {
    it.toUiNote()
}