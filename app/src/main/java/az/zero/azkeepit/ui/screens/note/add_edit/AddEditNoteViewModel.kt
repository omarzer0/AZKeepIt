package az.zero.azkeepit.ui.screens.note.add_edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.data.repository.FolderRepository
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.domain.mappers.UiFolder
import az.zero.azkeepit.domain.mappers.UiNote
import az.zero.azkeepit.ui.screens.navArgs
import az.zero.azkeepit.util.JDateTimeUtil
import az.zero.azkeepit.util.folderInitialId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@HiltViewModel
class AddEditNoteScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: AddEditNoteScreenArgs = savedStateHandle.navArgs()
    private val isNewNote = args.noteId == null
    private val localUiNote = MutableStateFlow(emptyUiNote)
    private val allFolders = folderRepository.getUiFolders()
    private val shouldPopUp = MutableStateFlow(false)
    private val deleteDialogOpened = MutableStateFlow(false)


    val state = combine(
        localUiNote,
        shouldPopUp,
        deleteDialogOpened,
        allFolders
    ) { localUiNote, shouldPopUp, isDeleteDialogOpened, allFolders ->
        AddEditNoteState(
            note = localUiNote,
            numberOfWordsForContent = localUiNote.content.length,
            isSaveActive = localUiNote.title.isNotBlank() || localUiNote.content.isNotBlank(),
            allFolders = allFolders,
            isNoteNew = isNewNote,
            shouldPopUp = shouldPopUp,
            isDeleteDialogOpened = isDeleteDialogOpened
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddEditNoteState()
    )

    fun saveNote() = viewModelScope.launch {
        val note = Note(
            title = state.value.note.title.trim(),
            content = state.value.note.content.trim(),
            isLocked = state.value.note.isLocked,
            createdAt = state.value.note.createdAt,
            ownerFolderId = state.value.note.ownerUiFolder?.folderId ?: folderInitialId,
            noteId = args.noteId
        )

        if (isNewNote) noteRepository.insertNote(note)
        else noteRepository.updateNote(note)
        shouldPopUp.emit(true)
    }

    fun changeDialogOpenState(isOpened: Boolean) = viewModelScope.launch {
        deleteDialogOpened.emit(isOpened)
    }

    fun updateTitle(text: String) = viewModelScope.launch {
        localUiNote.emit(localUiNote.value.copy(title = text))
    }

    fun updateContent(text: String) = viewModelScope.launch {
        localUiNote.emit(localUiNote.value.copy(content = text))
    }

    fun updateIsLocked(newValue: Boolean) = viewModelScope.launch {
        localUiNote.emit(localUiNote.value.copy(isLocked = newValue))
    }

    fun addNoteToFolder(newUiFolder: UiFolder) = viewModelScope.launch {
        localUiNote.emit(localUiNote.value.copy(ownerUiFolder = newUiFolder))
    }

    fun deleteNote() = viewModelScope.launch {
        val noteId = args.noteId ?: return@launch
        val deletedNoteId = noteRepository.deleteNote(noteId = noteId)
        if (deletedNoteId != -1) shouldPopUp.emit(true)
    }

    fun onBackPressed() = viewModelScope.launch {
        shouldPopUp.emit(true)
    }

    init {
        viewModelScope.launch {
            val noteId = args.noteId ?: return@launch
            val uiNote = noteRepository.getNoteById(noteId) ?: return@launch
            localUiNote.emit(uiNote)
        }
    }
}


data class AddEditNoteState(
    val note: UiNote = emptyUiNote,
    val isSaveActive: Boolean = false,
    val numberOfWordsForContent: Int = 0,
    val allFolders: List<UiFolder> = emptyList(),
    val isNoteNew: Boolean = false,
    val shouldPopUp: Boolean = false,
    val isDeleteDialogOpened: Boolean = false,
)

data class AddEditNoteScreenArgs(
    val noteId: Long?,
)

private val createdDate = JDateTimeUtil.now()
private val emptyUiNote = UiNote(
    "",
    "",
    false,
    createdDate,
    JDateTimeUtil.toShortDateTimeFormat(createdDate),
    JDateTimeUtil.toLongDateTimeFormat(createdDate),
    -1L,
    false,
    null
)