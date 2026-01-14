package az.zero.azkeepit.data.local.entities.folder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Folder")
data class DbFolder(
    val name: String,
    val createdAt: Long,
    @PrimaryKey(autoGenerate = true)
    val folderId: Long? = null,
)