package az.zero.azkeepit.data.mappers

import az.zero.azkeepit.data.local.entities.FolderWithNotes
import az.zero.azkeepit.data.local.entities.NoteWithFolder
import az.zero.azkeepit.data.local.entities.UiFolderWithUiNotes

fun FolderWithNotes?.toUiNoteWithUiFolder(): UiFolderWithUiNotes? {
        return if (this == null) return null
        else {
            UiFolderWithUiNotes(
                folder = this.folder,
                noteWithFolders = this.notes.map { note ->
                    NoteWithFolder(note = note, this.folder)
                }
            )
        }
    }