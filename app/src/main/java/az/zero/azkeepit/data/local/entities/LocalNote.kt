package az.zero.azkeepit.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity
data class LocalNote(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val date: Long,
    val time: Long,
    val folderName: String,
    val ownerFolderId: Long?,
    @PrimaryKey(autoGenerate = true)
    val noteId: Long? = null,
)

@Entity
data class LocalFolder(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val folderId: Long? = null,
)

data class LocalFolderWithNotes(
    @Embedded
    val localFolder: LocalFolder,
    @Relation(
        // Id of the parent (it is in one-to-many relation the table that has one relation)
        // like folder that has many notes but the notes has only one folder
        parentColumn = "folderId",
        // correspond to the field in LocalNote (the child)
        entityColumn = "ownerFolderId",
    )
    val notes: List<LocalNote>,
)