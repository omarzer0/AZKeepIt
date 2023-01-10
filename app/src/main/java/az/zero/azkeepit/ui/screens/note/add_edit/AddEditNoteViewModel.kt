package az.zero.azkeepit.ui.screens.note.add_edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.ui.screens.navArgs
import az.zero.azkeepit.util.JDateTimeUtil
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
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: AddEditNoteScreenArgs = savedStateHandle.navArgs()
    private var createdAt = JDateTimeUtil.now()

    private val title = MutableStateFlow("")
    private val content = MutableStateFlow("")
    private val isLocked = MutableStateFlow(false)
    private val dateTime = MutableStateFlow(JDateTimeUtil.toLongDateTimeFormat(createdAt))
    private val folder = MutableStateFlow(Folder("1", 1))

    val state = combine(
        title,
        content,
        isLocked,
        dateTime,
        folder
    ) { title, content, isLocked, dateTime, folder ->
        AddEditNoteState(
            title = title,
            content = content,
            isLocked = isLocked,
            numberOfWordsForContent = content.length,
            isSaveActive = title.isNotBlank() || content.isNotBlank(),
//            isSaveActive = (title.isNotBlank() || content.isNotBlank()) && folder.folderId != null,
            dateTime = dateTime,
            folder = folder
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddEditNoteState()
    )


    fun saveNote() = viewModelScope.launch {
        val isNewNote = args.noteId == null
        val note = Note(
            title = state.value.title,
            content = state.value.content,
            isLocked = state.value.isLocked,
            createdAt = createdAt,
            folderName = state.value.folder.name,
            ownerFolderId = state.value.folder.folderId,
            noteId = args.noteId
        )

        if (isNewNote) noteRepository.insertNote(note)
        else noteRepository.updateNote(note)
    }

    fun updateTitle(text: String) = viewModelScope.launch {
        title.emit(text)
    }

    fun updateContent(text: String) = viewModelScope.launch {
        content.emit(text)
    }

    fun updateFolder(newFolder: Folder) = viewModelScope.launch {
        folder.emit(newFolder)
    }

    fun updateIsLocked(newValue: Boolean) = viewModelScope.launch {
        isLocked.emit(newValue)
    }

    init {
        viewModelScope.launch {
            val noteId = args.noteId ?: return@launch

            // fixme if null handle this error
            val note = noteRepository.getNoteById(noteId) ?: return@launch
            title.emit(note.title)
            content.emit(note.content)
            createdAt = note.createdAt
            dateTime.emit(JDateTimeUtil.toLongDateTimeFormat(note.createdAt))
            folder.emit(Folder(name = note.folderName, folderId = note.ownerFolderId))
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
    val folder: Folder = Folder("", null),
)

data class AddEditNoteScreenArgs(
    val noteId: Long?,
)