package az.zero.azkeepit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val folderName: String,
    val ownerFolderId: Long?,
    @PrimaryKey(autoGenerate = true)
    val noteId: Long? = null,
)