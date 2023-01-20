package az.zero.azkeepit.data.repository

import az.zero.azkeepit.data.local.NoteDao
import az.zero.azkeepit.data.local.entities.*
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
) {

    fun getUiFolders() = noteDao.getFolders().map { it.toUiFolders() }

    fun getUiFolder(folderId: Long) = noteDao.getFolderWithNotesById(folderId).map {
        it?.toUiFolder()
    }

    suspend fun insertFolder(folder: Folder) = noteDao.insertFolder(folder)

    suspend fun deleteFolder(folderId: Long) = noteDao.deleteFolder(folderId)

    suspend fun renameFolder(folderId: Long, newName: String) =
        noteDao.renameFolder(folderId, newName)

    suspend fun deleteSelectedFolders(selectedFoldersId: MutableList<Long>) =
        noteDao.deleteSelectedFolders(selectedFoldersId)

    // ========================== Notes ======================


    fun getNotesWithFolderName() = noteDao.getNotesWithFolderName().map { it.toUiNotes() }

    suspend fun getNoteById(noteId: Long) = noteDao.getNoteById(noteId)?.toUiNote()

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(noteId: Long) = noteDao.deleteNote(noteId)

    fun searchNotes(query: String) = noteDao.searchNotes(query.trim()).map {
        it.toUiNotes()
    }

    suspend fun deleteAllNotesFromFolder(folderId: Long) =
        noteDao.deleteAllNotesFromFolder(folderId)

    suspend fun deleteAllSelectedNotes(noteIds: MutableList<Long>) =
        noteDao.deleteAllSelectedNotes(noteIds)

    suspend fun moveNotesToFolder(folderId: Long, selectedNotesIds: MutableList<Long>) =
        noteDao.moveNotesToFolder(folderId, selectedNotesIds)

}