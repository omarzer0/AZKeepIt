package az.zero.azkeepit.data.local

import androidx.room.*
import az.zero.azkeepit.data.local.entities.DbFolder
import az.zero.azkeepit.data.local.entities.DbFolderWithNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Query("SELECT * FROM Folder ORDER BY createdAt DESC")
    fun getFolders(): Flow<List<DbFolder>>

    @Transaction
    @Query("SELECT * FROM Folder ORDER BY createdAt DESC")
    fun getFoldersWithNotes(): Flow<List<DbFolderWithNotes>>

    @Query("SELECT * FROM Folder WHERE folderId=:folderId")
    suspend fun getFolderById(folderId: Long): DbFolder?

    @Transaction
    @Query("SELECT * FROM Folder WHERE folderId=:folderId")
    fun getFolderWithNotesById(folderId: Long): Flow<DbFolderWithNotes?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFolder(dbFolder: DbFolder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFolder(dbFolder: DbFolder)

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