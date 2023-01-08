package az.zero.azkeepit.data.local

import androidx.room.*
import az.zero.azkeepit.data.local.entities.LocalFolder
import az.zero.azkeepit.data.local.entities.LocalFolderWithNotes
import az.zero.azkeepit.data.local.entities.LocalNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM LocalFolder")
    fun getFoldersWithNotes(): Flow<List<LocalFolderWithNotes>>

    @Query("SELECT * FROM LocalFolder")
    fun getFolders(): Flow<List<LocalFolder>>

    @Transaction
    @Query("SELECT * FROM LocalFolder WHERE folderId=:folderId")
    fun getFolderWithNotesById(folderId: Long): Flow<LocalFolderWithNotes>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFolder(localFolder: LocalFolder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFolder(localFolder: LocalFolder)

    @Delete
    suspend fun deleteFolder(localFolder: LocalFolder)


    // ========================== Notes ======================

    @Query("SELECT * FROM LocalNote")
    fun getNotes(): Flow<List<LocalNote>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(localNote: LocalNote)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(localNote: LocalNote)

    @Delete
    suspend fun deleteNote(localNote: LocalNote)

}