package az.zero.azkeepit.data.local

import androidx.room.*
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.FolderWithNotes
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.data.local.entities.NoteWithFolder
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

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

    @Query("DELETE FROM Folder WHERE folderId IN (:selectedFoldersId)")
    suspend fun deleteSelectedFolders(selectedFoldersId: MutableList<Long>)


}