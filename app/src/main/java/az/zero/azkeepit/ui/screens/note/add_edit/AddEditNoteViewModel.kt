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
import az.zero.azkeepit.ui.screens.navArgs
import az.zero.azkeepit.util.JDateTimeUtil
import az.zero.azkeepit.util.combine
import az.zero.azkeepit.util.folderInitialId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
    private var createdAt = JDateTimeUtil.now()

    private val title = MutableStateFlow("")
    private val content = MutableStateFlow("")
    private val isLocked = MutableStateFlow(false)
    private val dateTime = MutableStateFlow(JDateTimeUtil.toLongDateTimeFormat(createdAt))
    private val allFolders = folderRepository.getUiFolders()
    private val folder = MutableStateFlow<UiFolder?>(null)
    private val shouldPopUp = MutableStateFlow(false)
    private val deleteDialogOpened = MutableStateFlow(false)

    val state = combine(
        title,
        content,
        isLocked,
        dateTime,
        folder,
        allFolders,
        shouldPopUp,
        deleteDialogOpened
    ) { title, content, isLocked, dateTime, folder, allFolders, shouldPopUp, isDeleteDialogOpened ->
        AddEditNoteState(
            title = title,
            content = content,
            isLocked = isLocked,
            numberOfWordsForContent = content.length,
            isSaveActive = title.isNotBlank() || content.isNotBlank(),
            dateTime = dateTime,
            folder = folder,
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
            title = state.value.title.trim(),
            content = state.value.content.trim(),
            isLocked = state.value.isLocked,
            createdAt = createdAt,
            ownerFolderId = state.value.folder?.folderId ?: folderInitialId,
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
        title.emit(text)
    }

    fun updateContent(text: String) = viewModelScope.launch {
        content.emit(text)
    }

    fun updateIsLocked(newValue: Boolean) = viewModelScope.launch {
        isLocked.emit(newValue)
    }

    fun addNoteToFolder(newUiFolder: UiFolder) = viewModelScope.launch {
        folder.emit(newUiFolder)
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
            title.emit(uiNote.title)
            content.emit(uiNote.content)
            createdAt = uiNote.createdAt
            folder.emit(uiNote.ownerUiFolder)
            dateTime.emit(JDateTimeUtil.toLongDateTimeFormat(uiNote.createdAt))
        }
    }
}


data class AddEditNoteState(
    val title: String = "",
    val content: String = "",
    val isLocked: Boolean = false,
    val numberOfWordsForContent: Int = 0,
    val isSaveActive: Boolean = false,
    val dateTime: String = "",
    val folder: UiFolder? = null,
    val allFolders: List<UiFolder> = emptyList(),
    val isNoteNew: Boolean = false,
    val shouldPopUp: Boolean = false,
    val isDeleteDialogOpened: Boolean = false,
)

data class AddEditNoteScreenArgs(
    val noteId: Long?,
)