package az.zero.azkeepit.data.repository

import az.zero.azkeepit.data.local.NoteDao
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.Note
import kotlinx.coroutines.flow.filter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
) {

    fun getFolders() = noteDao.getFolders()

    fun getFoldersWithNotes() = noteDao.getFoldersWithNotes()

    fun getFolderWithNotesById(folderId: Long) = noteDao.getFolderWithNotesById(folderId)

    suspend fun insertFolder(folder: Folder) = noteDao.insertFolder(folder)

    suspend fun updateFolder(folder: Folder) = noteDao.updateFolder(folder)

    suspend fun deleteFolder(folder: Folder) = noteDao.deleteFolder(folder)


    // ========================== Notes ======================


    fun getNotes() = noteDao.getNotes()

    suspend fun getNoteById(noteId: Long) = noteDao.getNoteById(noteId)

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

}