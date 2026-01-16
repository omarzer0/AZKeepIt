package az.zero.azkeepit.ui.mappers.note

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import az.zero.azkeepit.domain.models.note.DomainNote
import az.zero.azkeepit.ui.mappers.folder.toDomainFolder
import az.zero.azkeepit.ui.mappers.folder.toUiFolder
import az.zero.azkeepit.ui.models.note.UiNote
import az.zero.azkeepit.util.JDateTimeUtil


fun DomainNote.toUiNote(isSelected : Boolean = false) = UiNote(
    noteId = this.noteId,
    title = this.title,
    content = this.content,
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    images = this.images.map { Uri.parse(it) },
    color = Color(this.colorArgb),
    password = hashedPassword,
    shortDateTime = JDateTimeUtil.toShortDateTimeFormat(this.createdAt),
    longDateTime = JDateTimeUtil.toLongDateTimeFormat(this.createdAt),
    isSelected = isSelected,
    ownerUiFolder = this.ownerDomainFolder?.toUiFolder()
)

fun UiNote.toDomainNote() = DomainNote(
    noteId = this.noteId,
    title = this.title.trim(),
    content = this.content.trim(),
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    images = this.images.map { it.toString() },
    colorArgb = this.color.toArgb(),
    hashedPassword = this.password,
    ownerDomainFolder = this.ownerUiFolder?.toDomainFolder()
)