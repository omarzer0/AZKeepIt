package az.zero.azkeepit.data.repository

import az.zero.azkeepit.data.local.NoteDao
import az.zero.azkeepit.data.mappers.*
import az.zero.azkeepit.domain.model.UiFolder
import az.zero.azkeepit.domain.model.UiNote
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
) {

    fun getFolders() = noteDao.getFolders().toFlowListOfUiFolders()

    fun getFoldersWithNotes() = noteDao.getFoldersWithNotes().toFlowListOfUiFolderWithNotes()

    fun getFolderWithNotesById(folderId: Long) = noteDao.getFolderWithNotesById(folderId).map {
        it.toUiFolderWithNotes()
    }

    suspend fun insertFolder(uiFolder: UiFolder) = noteDao.insertFolder(uiFolder.toLocalFolder())

    suspend fun updateFolder(uiFolder: UiFolder) = noteDao.updateFolder(uiFolder.toLocalFolder())

    suspend fun deleteFolder(uiFolder: UiFolder) = noteDao.deleteFolder(uiFolder.toLocalFolder())


    // ========================== Notes ======================


    fun getNotes() = noteDao.getNotes().toFlowListOfUiNote()

    suspend fun getNoteById(noteId:Long) = noteDao.getNoteById(noteId)?.toUiNote()

    suspend fun insertNote(uiNote: UiNote) = noteDao.insertNote(uiNote.toLocalNote())

    suspend fun updateNote(uiNote: UiNote) = noteDao.updateNote(uiNote.toLocalNote())

    suspend fun deleteNote(uiNote: UiNote) = noteDao.deleteNote(uiNote.toLocalNote())

}