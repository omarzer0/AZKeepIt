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
import az.zero.azkeepit.util.combine
import az.zero.azkeepit.util.folderInitialId
import az.zero.azkeepit.util.folderInitialName
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
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: AddEditNoteScreenArgs = savedStateHandle.navArgs()
    private var createdAt = JDateTimeUtil.now()

    private val title = MutableStateFlow("")
    private val content = MutableStateFlow("")
    private val isLocked = MutableStateFlow(false)
    private val dateTime = MutableStateFlow(JDateTimeUtil.toLongDateTimeFormat(createdAt))
    private val allFolders = noteRepository.getFolders()
    private val folder = MutableStateFlow<Folder?>(null)

    val state = combine(
        title,
        content,
        isLocked,
        dateTime,
        folder,
        allFolders
    ) { title, content, isLocked, dateTime, folder, allFolders ->
        AddEditNoteState(
            title = title,
            content = content,
            isLocked = isLocked,
            numberOfWordsForContent = content.length,
            isSaveActive = title.isNotBlank() || content.isNotBlank(),
            dateTime = dateTime,
            folder = folder,
            allFolders = allFolders
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddEditNoteState()
    )

    fun saveNote() = viewModelScope.launch {
        val isNewNote = args.noteId == null
        val note = Note(
            title = state.value.title.trim(),
            content = state.value.content.trim(),
            isLocked = state.value.isLocked,
            createdAt = createdAt,
            folderName = state.value.folder?.name ?: folderInitialName,
            ownerFolderId = state.value.folder?.folderId ?: folderInitialId,
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

    fun updateIsLocked(newValue: Boolean) = viewModelScope.launch {
        isLocked.emit(newValue)
    }

    fun addNoteToFolder(newFolder: Folder) = viewModelScope.launch {
        folder.emit(newFolder)
    }

    init {
        viewModelScope.launch {
            val noteId = args.noteId ?: return@launch
            val note = noteRepository.getNoteById(noteId) ?: return@launch
            title.emit(note.title)
            content.emit(note.content)
            createdAt = note.createdAt
            dateTime.emit(JDateTimeUtil.toLongDateTimeFormat(note.createdAt))
//            folder.emit(Folder(name = note.folderName,folderId = note.ownerFolderId))
            val ownerNoteId = note.ownerFolderId ?: return@launch
            val noteFolder = noteRepository.getFolderById(ownerNoteId) ?: return@launch
            folder.emit(noteFolder)
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
    val folder: Folder? = null,
    val allFolders: List<Folder> = emptyList(),
)

data class AddEditNoteScreenArgs(
    val noteId: Long?,
)