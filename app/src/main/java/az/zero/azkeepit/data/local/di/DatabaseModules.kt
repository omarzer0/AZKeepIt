package az.zero.azkeepit.data.local.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import az.zero.azkeepit.BuildConfig.DEBUG
import az.zero.azkeepit.data.local.AppDatabase
import az.zero.azkeepit.data.local.FolderDao
import az.zero.azkeepit.data.local.NoteDao
import az.zero.azkeepit.data.local.helper.CustomTypeConverters
import az.zero.azkeepit.domain.hashing.PasswordHasher
import createMigration1To2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModules {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        passwordHasher: PasswordHasher
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .apply {
                addMigrations(createMigration1To2(passwordHasher))
                addTypeConverter(CustomTypeConverters())
                if (DEBUG) {
                    addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate database on first creation
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                populateInitialData(db, 100)
                            }
                        }
                    })
                }
            }
            .build()

    @Singleton
    @Provides
    fun providesNoteDao(appDatabase: AppDatabase): NoteDao = appDatabase.getNoteDao()

    @Singleton
    @Provides
    fun providesFolderDao(appDatabase: AppDatabase): FolderDao = appDatabase.getFolderDao()

    private fun populateInitialData(db: SupportSQLiteDatabase, count: Int) {
        val currentTime = System.currentTimeMillis()

        for (x in 1..count) {
            val color = getColorForNote(x)
            val createdAt = currentTime - (count - x) * 60000L

            db.execSQL(
                """
                INSERT INTO Note (noteId, title, content, isLocked, createdAt, ownerFolderId, images, color, hashedPassword)
                VALUES ($x, 'title $x', 'content $x', 0, $createdAt, NULL, '[]', $color, NULL)
                """.trimIndent()
            )
            Log.d("DatabaseModulesTag", "note #$x inserted")
        }
    }

    private fun getColorForNote(index: Int): Int {
        val colors = listOf(
            0xFFFFFFFF.toInt(), // White
            0xFFFFE4E1.toInt(), // Light pink
            0xFFE1F5FF.toInt(), // Light blue
            0xFFE8F5E9.toInt(), // Light green
            0xFFFFF9C4.toInt(), // Light yellow
            0xFFFFE0B2.toInt(), // Light orange
        )
        return colors[index % colors.size]
    }

}
