package az.zero.azkeepit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import az.zero.azkeepit.data.local.entities.LocalFolder
import az.zero.azkeepit.data.local.entities.LocalNote

@Database(
    entities = [LocalNote::class, LocalFolder::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "az_keep_it_app"
    }
}