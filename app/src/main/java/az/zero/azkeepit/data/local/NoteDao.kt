package az.zero.azkeepit.data.local

import androidx.room.*
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.FolderWithNotes
import az.zero.azkeepit.data.local.entities.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM Folder ORDER BY createdAt DESC")
    fun getFolders(): Flow<List<Folder>>

    @Transaction
    @Query("SELECT * FROM Folder ORDER BY createdAt DESC")
    fun getFoldersWithNotes(): Flow<List<FolderWithNotes>>

    @Query("SELECT * FROM Folder WHERE folderId=:folderId")
    suspend fun getFolderById(folderId: Long): Folder?

    @Transaction
    @Query("SELECT * FROM Folder WHERE folderId=:folderId")
    fun getFolderWithNotesById(folderId: Long): Flow<FolderWithNotes>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFolder(folder: Folder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFolder(folder: Folder)

    @Delete
    suspend fun deleteFolder(folder: Folder)


    // ========================== Notes ======================

    @Query("SELECT * FROM Note ORDER BY createdAt DESC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE noteId=:noteId")
    suspend fun getNoteById(noteId: Long): Note?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

}