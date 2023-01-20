package az.zero.azkeepit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.Note

@Database(
    entities = [Note::class, Folder::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao
    abstract fun getFolderDao(): FolderDao

    companion object {
        const val DATABASE_NAME = "az_keep_it_app"
    }
}