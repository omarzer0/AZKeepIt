package az.zero.azkeepit.data.local.entities

import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import az.zero.azkeepit.domain.mappers.UiFolder

@Entity
data class Note(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val ownerFolderId: Long?,
    val images :List<String>,
    val color : Int,
    @PrimaryKey(autoGenerate = true)
    val noteId: Long? = null,
)


data class NoteWithFolder(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "ownerFolderId",
        entityColumn = "folderId"
    )
    val folder: Folder?,
)