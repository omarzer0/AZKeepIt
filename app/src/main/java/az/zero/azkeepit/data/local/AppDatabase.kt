package az.zero.azkeepit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import az.zero.azkeepit.data.local.entities.folder.DbFolder
import az.zero.azkeepit.data.local.entities.note.DbNote
import az.zero.azkeepit.data.local.helper.CustomTypeConverters

@Database(
    entities = [DbNote::class, DbFolder::class],
    version = 2
)
@TypeConverters(CustomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao
    abstract fun getFolderDao(): FolderDao

    companion object {
        const val DATABASE_NAME = "az_keep_it_app"
    }
}