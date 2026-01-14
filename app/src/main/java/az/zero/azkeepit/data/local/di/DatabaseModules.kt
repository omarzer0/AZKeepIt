package az.zero.azkeepit.data.local.di

import android.content.Context
import androidx.room.Room
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
            }
            .build()

    @Singleton
    @Provides
    fun providesNoteDao(appDatabase: AppDatabase): NoteDao = appDatabase.getNoteDao()

    @Singleton
    @Provides
    fun providesFolderDao(appDatabase: AppDatabase): FolderDao = appDatabase.getFolderDao()
}
