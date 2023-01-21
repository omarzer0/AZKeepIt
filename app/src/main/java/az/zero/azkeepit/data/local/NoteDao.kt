package az.zero.azkeepit.data.local

import androidx.room.*
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.FolderWithNotes
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.data.local.entities.NoteWithFolder
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

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

    @Query("UPDATE Note SET ownerFolderId=:folderId WHERE noteId in (:selectedNotesIds)")
    suspend fun moveNotesToFolder(folderId: Long, selectedNotesIds: MutableList<Long>)


}