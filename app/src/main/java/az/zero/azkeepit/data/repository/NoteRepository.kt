package az.zero.azkeepit.data.repository

import az.zero.azkeepit.data.hashing.SHA256PasswordHasher
import az.zero.azkeepit.data.local.NoteDao
import az.zero.azkeepit.data.local.entities.note.DbNote
import az.zero.azkeepit.data.mappers.note.toDbNote
import az.zero.azkeepit.data.mappers.note.toDomainNote
import az.zero.azkeepit.ui.mappers.note.toDomainNote
import az.zero.azkeepit.ui.mappers.note.toUiNote
import az.zero.azkeepit.ui.models.note.UiNote
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val passwordHasher: SHA256PasswordHasher
) {

    fun getUiNotes() = noteDao.getNotesWithFolderName().map {
        it.map { it.toDomainNote().toUiNote() }
    }

    suspend fun getNoteById(noteId: Long) =
        noteDao.getNoteWithFolderById(noteId)?.toDomainNote()?.toUiNote()


    suspend fun saveNote(isNewNote: Boolean, uiNote: UiNote) {
        // TODO(improvement) Consider getting isNew from DB if the id doesn't exist = new
        val dbNote = uiNote.toDomainNote().toDbNote()
        if (isNewNote) insertNote(
            dbNote.copy(hashedPassword = passwordHasher.hash(dbNote.hashedPassword))
        )
        else updateNote(dbNote)
    }

    private suspend fun insertNote(dbNote: DbNote) = noteDao.insertNote(dbNote)

    private suspend fun updateNote(dbNote: DbNote) = noteDao.updateNote(dbNote)

    suspend fun deleteNote(noteId: Long) = noteDao.deleteNote(noteId)

    fun searchNotes(query: String) = noteDao.searchNotes(query.trim()).map {
        it.map { it.toDomainNote().toUiNote() }
    }

    suspend fun deleteAllNotesFromFolder(folderId: Long) =
        noteDao.deleteAllNotesFromFolder(folderId)

    suspend fun deleteAllSelectedNotes(noteIds: MutableList<Long>) =
        noteDao.deleteAllSelectedNotes(noteIds)

    suspend fun moveNotesToFolder(folderId: Long, selectedNotesIds: MutableList<Long>) =
        noteDao.moveNotesToFolder(folderId, selectedNotesIds)

    private suspend fun getStoredHashForNoteById(noteId: Long): String? =
        noteDao.getPasswordHash(noteId)

    suspend fun verifyPassword(noteId: Long, enteredPassword: String): Boolean {
        val storedHash = getStoredHashForNoteById(noteId) ?: ""
        return passwordHasher.verify(enteredPassword, storedHash)
    }

}