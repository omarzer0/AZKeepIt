package az.zero.azkeepit.data.local.entities.folder

import androidx.room.Embedded
import androidx.room.Relation
import az.zero.azkeepit.data.local.entities.note.DbNote

data class DbFolderWithNotes(
    @Embedded
    val dbFolder: DbFolder,
    @Relation(
        // Id of the parent (it is in one-to-many relation the table that has one relation)
        // like folder that has many notes but the notes has only one folder
        parentColumn = "folderId",
        // correspond to the field in LocalNote (the child)
        entityColumn = "ownerFolderId",
    )
    val dbNotes: List<DbNote>,
)