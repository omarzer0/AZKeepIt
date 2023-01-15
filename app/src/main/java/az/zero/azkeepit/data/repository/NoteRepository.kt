package az.zero.azkeepit.data.repository

import az.zero.azkeepit.data.local.NoteDao
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.Note
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
) {

    fun getFolders() = noteDao.getFolders().map { list -> list.filter { it.folderId != null } }

    fun getFoldersWithNotes() = noteDao.getFoldersWithNotes()

    suspend fun getFolderById(folderId: Long) = noteDao.getFolderById(folderId)

    fun getFolderWithNotesById(folderId: Long) = noteDao.getFolderWithNotesById(folderId)

    suspend fun insertFolder(folder: Folder) = noteDao.insertFolder(folder)

    suspend fun updateFolder(folder: Folder) = noteDao.updateFolder(folder)

    suspend fun deleteFolder(folderId: Long) = noteDao.deleteFolder(folderId)


    suspend fun renameFolder(folderId: Long, newName: String) =
        noteDao.renameFolder(folderId, newName)

    // ========================== Notes ======================


    fun getNotes() = noteDao.getNotes()


    suspend fun getNoteById(noteId: Long) = noteDao.getNoteById(noteId)

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(noteId: Long) = noteDao.deleteNote(noteId)

    fun searchNotes(query: String) = noteDao.searchNotes(query.trim())

    suspend fun deleteAllNotesFromFolder(folderId: Long) =
        noteDao.deleteAllNotesFromFolder(folderId)
}