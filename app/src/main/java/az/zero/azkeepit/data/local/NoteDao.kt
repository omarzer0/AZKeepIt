package az.zero.azkeepit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.data.local.entities.NoteWithFolder
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM Note ORDER BY createdAt DESC")
    fun getNotesWithFolderName(): Flow<List<NoteWithFolder>>

    @Query("SELECT * FROM Note WHERE noteId=:noteId")
    suspend fun getNoteWithFolderById(noteId: Long): NoteWithFolder?

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

    @Query("SELECT password FROM note WHERE noteId = :noteId")
    suspend fun getPasswordHash(noteId: Long): String?


}