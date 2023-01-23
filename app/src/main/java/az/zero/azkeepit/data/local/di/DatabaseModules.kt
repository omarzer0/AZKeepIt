package az.zero.azkeepit.data.local.di

import android.content.Context
import androidx.room.Room
import az.zero.azkeepit.BuildConfig
import az.zero.azkeepit.data.local.AppDatabase
import az.zero.azkeepit.data.local.DatabaseCallback
import az.zero.azkeepit.data.local.FolderDao
import az.zero.azkeepit.data.local.NoteDao
import az.zero.azkeepit.data.local.helper.CustomTypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModules {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        databaseCallback: DatabaseCallback,
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .apply {
                addCallback(databaseCallback)
                addTypeConverter(CustomTypeConverters())
                if (BuildConfig.DEBUG) {
                    fallbackToDestructiveMigration()
                }
            }
            .build()

    @Singleton
    @Provides
    fun providesNoteDao(appDatabase: AppDatabase): NoteDao = appDatabase.getNoteDao()

    @Singleton
    @Provides
    fun providesFolderDao(appDatabase: AppDatabase): FolderDao = appDatabase.getFolderDao()
}
