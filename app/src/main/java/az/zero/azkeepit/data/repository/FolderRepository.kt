package az.zero.azkeepit.data.repository

import az.zero.azkeepit.data.local.FolderDao
import az.zero.azkeepit.data.local.entities.folder.DbFolder
import az.zero.azkeepit.data.mappers.folder.toDomainFolder
import az.zero.azkeepit.data.mappers.folder.toDomainFolderWithNotes
import az.zero.azkeepit.ui.mappers.folder.toUiFolder
import az.zero.azkeepit.ui.mappers.folder.toUiFolderWithNote
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepository @Inject constructor(
    private val folderDao: FolderDao,
) {

    // TODO(Improvement) use list mapper ext fun to map

    fun getUiFolders() = folderDao.getFolders().map {
        it.map { it.toDomainFolder().toUiFolder()}
    }

    fun getUiFoldersWithNotes() = folderDao.getFoldersWithNotes().map {
        it.map { it.toDomainFolderWithNotes().toUiFolderWithNote() }
    }

    fun getUiFolderWithNotesById(folderId: Long) = folderDao.getFolderWithNotesById(folderId).map {
        it?.toDomainFolderWithNotes()?.toUiFolderWithNote()
    }

    suspend fun insertFolder(dbFolder: DbFolder) = folderDao.insertFolder(dbFolder)

    suspend fun deleteFolder(folderId: Long) = folderDao.deleteFolder(folderId)

    suspend fun renameFolder(folderId: Long, newName: String) =
        folderDao.renameFolder(folderId, newName)

    suspend fun deleteSelectedFolders(selectedFoldersId: MutableList<Long>) =
        folderDao.deleteSelectedFolders(selectedFoldersId)

}