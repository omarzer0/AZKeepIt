package az.zero.azkeepit.ui.models.folder

import az.zero.azkeepit.ui.models.note.UiNote


data class UiFolderWithNotes(
    val folder: UiFolder,
    val notes: List<UiNote>
)