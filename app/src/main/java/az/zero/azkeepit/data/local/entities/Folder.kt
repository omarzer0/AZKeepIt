package az.zero.azkeepit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Folder(
    val name: String,
    val createdAt: Long,
    @PrimaryKey(autoGenerate = true)
    val folderId: Long? = null,
)