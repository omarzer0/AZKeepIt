package az.zero.azkeepit.data.mappers

import az.zero.azkeepit.data.local.entities.FolderWithNotes
import az.zero.azkeepit.data.local.entities.UiNote
import az.zero.azkeepit.data.local.entities.UiFolderWithUiNotes

fun FolderWithNotes?.toUiNoteWithUiFolder(): UiFolderWithUiNotes? {
        return if (this == null) return null
        else {
            UiFolderWithUiNotes(
                uiFolder = this.folder,
                uiNotes = this.notes.map { note ->
                    UiNote(note = note, this.folder)
                }
            )
        }
    }