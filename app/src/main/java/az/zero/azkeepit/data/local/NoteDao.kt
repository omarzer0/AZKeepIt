package az.zero.azkeepit.data.local

import androidx.room.*
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.FolderWithNotes
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.data.local.entities.NoteWithFolder
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
    fun getFolderWithNotesById(folderId: Long): Flow<FolderWithNotes?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFolder(folder: Folder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFolder(folder: Folder)

    @Query("DELETE FROM Folder WHERE folderId =:folderId")
    suspend fun deleteFolder(folderId: Long)

    @Query("DELETE FROM Folder WHERE folderId =:folderId")
    suspend fun deleteFolderById(folderId: Long)

    @Query("UPDATE Note SET ownerFolderId= -1 WHERE ownerFolderId=:folderId")
    suspend fun renameAllNotesOfFolder(folderId: Long)

    @Query("UPDATE Folder SET name =:newName WHERE folderId =:folderId")
    suspend fun renameFolder(folderId: Long, newName: String)

    // ========================== Notes ======================

    @Transaction
    @Query("SELECT * FROM Note ORDER BY createdAt DESC")
    fun getNotesWithFolderName(): Flow<List<NoteWithFolder>>

    @Query("SELECT * FROM Note WHERE noteId=:noteId")
    suspend fun getNoteById(noteId: Long): NoteWithFolder?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM Note WHERE noteId=:noteId")
    suspend fun deleteNote(noteId: Long): Int

    @Query("SELECT * FROM Note WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%' ORDER BY createdAt DESC ")
    fun searchNotes(searchQuery: String): Flow<List<NoteWithFolder>>

    @Query("DELETE FROM Note WHERE ownerFolderId=:folderId")
    suspend fun deleteAllNotesFromFolder(folderId: Long)

    @Query("DELETE FROM Note WHERE noteId in (:noteIds)")
    suspend fun deleteAllSelectedNotes(noteIds: MutableList<Long>)


}