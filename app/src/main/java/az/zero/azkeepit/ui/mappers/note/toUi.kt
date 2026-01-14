package az.zero.azkeepit.ui.mappers.note

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import az.zero.azkeepit.domain.models.note.DomainNote
import az.zero.azkeepit.domain.models.note.DomainNoteWithFolder
import az.zero.azkeepit.ui.mappers.folder.toUi
import az.zero.azkeepit.ui.models.folder.UiFolder
import az.zero.azkeepit.ui.models.note.UiNote
import az.zero.azkeepit.ui.models.note.UiNoteWithFolder
import az.zero.azkeepit.util.JDateTimeUtil

// DomainNote → UiNote
fun DomainNote.toUi(
    isSelected: Boolean = false,
    ownerUiFolder: UiFolder? = null
) = UiNote(
    noteId = this.noteId,
    title = this.title,
    content = this.content,
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    images = this.images.map { Uri.parse(it) },
    color = Color(this.colorArgb),
    hashedPassword = this.hashedPassword,
    shortDateTime = JDateTimeUtil.toShortDateTimeFormat(this.createdAt),
    longDateTime = JDateTimeUtil.toLongDateTimeFormat(this.createdAt),
    isSelected = isSelected,
    ownerUiFolder = ownerUiFolder
)

// UiNote → DomainNote
fun UiNote.toDomain() = DomainNote(
    noteId = this.noteId,
    title = this.title.trim(),
    content = this.content.trim(),
    isLocked = this.isLocked,
    createdAt = this.createdAt,
    ownerFolderId = this.ownerUiFolder?.folderId,
    images = this.images.map { it.toString() },
    colorArgb = this.color.toArgb(),
    hashedPassword = this.hashedPassword
)

// DomainNoteWithFolder → UiNoteWithFolder
fun DomainNoteWithFolder.toUi(isSelected: Boolean = false) = UiNoteWithFolder(
    note = this.note.toUi(
        isSelected = isSelected,
        ownerUiFolder = this.folder?.toUi()
    ),
    folder = this.folder?.toUi()
)

// List extensions
fun List<DomainNote>.toUi() = map { it.toUi() }
fun List<DomainNoteWithFolder>.toUiNotes() = map { it.toUi() }