package az.zero.azkeepit.data.repository

import az.zero.azkeepit.data.local.FolderDao
import az.zero.azkeepit.data.local.entities.folder.DbFolder
import kotlinx.coroutines.flow.map
import az.zero.azkeepit.domain.mappers.toUiFolder
import az.zero.azkeepit.domain.mappers.toUiFolders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepository @Inject constructor(
    private val folderDao: FolderDao,
) {

    fun getUiFolders() = folderDao.getFoldersWithNotes().map { it.toUiFolders() }

    fun getUiFolderById(folderId: Long) = folderDao.getFolderWithNotesById(folderId).map {
        it?.toUiFolder()
    }

    suspend fun insertFolder(dbFolder: DbFolder) = folderDao.insertFolder(dbFolder)

    suspend fun deleteFolder(folderId: Long) = folderDao.deleteFolder(folderId)

    suspend fun renameFolder(folderId: Long, newName: String) =
        folderDao.renameFolder(folderId, newName)

    suspend fun deleteSelectedFolders(selectedFoldersId: MutableList<Long>) =
        folderDao.deleteSelectedFolders(selectedFoldersId)

}