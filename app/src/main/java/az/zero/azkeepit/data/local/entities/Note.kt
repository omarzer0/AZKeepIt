package az.zero.azkeepit.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Note")
data class Note(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val ownerFolderId: Long?,
    val images: List<String>,
    val color: Int,
    val hashedPassword: String?,
    @PrimaryKey(autoGenerate = true)
    val noteId: Long? = null,
)

@Entity(tableName = "NoteWithFolder")
data class NoteWithFolder(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "ownerFolderId",
        entityColumn = "folderId"
    )
    val folder: Folder?,
)