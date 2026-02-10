package az.zero.azkeepit.data.local.entities.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Note")
data class DbNote(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val ownerFolderId: Long?,
    val images: List<String>,
    val color: Int,
    val hashedPassword: String?,
    /* TODO(improve) 0L instead of null is considered no set and Room will
        auto generate it when annotated https://developer.android.com/reference/androidx/room/PrimaryKey#getAutoGenerate()
    */
    @PrimaryKey(autoGenerate = true)
    val noteId: Long? = null,
)