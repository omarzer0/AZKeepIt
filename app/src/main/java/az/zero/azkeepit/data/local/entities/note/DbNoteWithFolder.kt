package az.zero.azkeepit.data.local.entities.note

import androidx.room.Embedded
import androidx.room.Relation
import az.zero.azkeepit.data.local.entities.folder.DbFolder

data class DbNoteWithFolder(
    @Embedded val dbNote: DbNote,
    @Relation(
        parentColumn = "ownerFolderId",
        entityColumn = "folderId"
    )
    val dbFolder: DbFolder?,
)