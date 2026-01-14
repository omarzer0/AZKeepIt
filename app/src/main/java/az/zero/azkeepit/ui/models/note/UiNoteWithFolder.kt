package az.zero.azkeepit.ui.models.note

import az.zero.azkeepit.ui.models.folder.UiFolder

data class UiNoteWithFolder(
    val note: UiNote,
    val folder: UiFolder?
)