package az.zero.azkeepit.data.local.entities

import androidx.room.*


data class FolderWithNotes(
    @Embedded
    val folder: Folder,
    @Relation(
        // Id of the parent (it is in one-to-many relation the table that has one relation)
        // like folder that has many notes but the notes has only one folder
        parentColumn = "folderId",
        // correspond to the field in LocalNote (the child)
        entityColumn = "ownerFolderId",
    )
    val notes: List<Note>,
)



data class NoteWithFolder(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "ownerFolderId",
        entityColumn = "folderId"
    )
    val folder: Folder?
)