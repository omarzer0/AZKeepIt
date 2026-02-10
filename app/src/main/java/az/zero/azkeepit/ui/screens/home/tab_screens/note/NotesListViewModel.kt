package az.zero.azkeepit.ui.screens.home.tab_screens.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.repository.FolderRepository
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.ui.models.folder.UiFolder
import az.zero.azkeepit.ui.models.note.UiNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
) : ViewModel() {

    // TODO(FIXME) When edit mode is open and back btn is
    //  clicked the edit mode should close not finish the app

    // TODO(FIXME) The selected notes don't enable delete and move to folder

    private val isDeleteNotesDialogOpened = MutableStateFlow(false)
    private val selectedNotes = MutableStateFlow(mutableListOf<Long>())

    private val uiNotes = noteRepository.getUiNotes()
    private val uiFolders = folderRepository.getUiFolders()

    fun changeDeleteNotesDialogState(isOpened: Boolean) {
        viewModelScope.launch {
            isDeleteNotesDialogOpened.emit(isOpened)
        }
    }

    fun onMoveSelectedNotesToFolder(folderId: Long) {
        viewModelScope.launch {
            noteRepository.moveNotesToFolder(
                folderId = folderId,
                selectedNotesIds = selectedNotes.value
            )
            selectedNotes.emit(mutableListOf())
        }
    }

    fun onAddOrRemoveNoteFromSelected(noteId: Long?) {
        viewModelScope.launch {
            if (noteId == null) return@launch
            val notes = selectedNotes.value.toMutableList()
            val exist = notes.any { noteId == it }
            if (exist) notes.remove(noteId)
            else notes.add(noteId)
            selectedNotes.emit(notes)
        }
    }

    fun onChangeDeleteNotesDialogState(isBottomActionOpened: Boolean) {
        viewModelScope.launch {
            isDeleteNotesDialogOpened.emit(isBottomActionOpened)
        }
    }

    fun onClearSelectedNotes() {
        viewModelScope.launch {
            selectedNotes.emit(mutableListOf())
        }
    }


    val state = combine(
        uiNotes,
        uiFolders,
        selectedNotes,
        isDeleteNotesDialogOpened,
    ) { uiNotes, uiFolders,
        selectedNotes, isDeleteNotesDialogOpened ->
        NotesListState(
            isEmpty = uiNotes.isEmpty(),
            isLoading = false,
            numberOfSelectedNotes = selectedNotes.size,
            isDeleteNotesDialogOpen = isDeleteNotesDialogOpened,
            isNoteActionsEnabled = selectedNotes.isNotEmpty(),
            uiFolders = uiFolders,
            uiNotes = uiNotes.map { it.copy(isSelected = selectedNotes.contains(it.noteId)) },
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        NotesListState()
    )

}

data class NotesListState(
    val isEmpty: Boolean = false,
    val isLoading: Boolean = true,
    val isDeleteNotesDialogOpen: Boolean = false,
    val isNoteActionsEnabled: Boolean = false,
    val numberOfSelectedNotes: Int = 0,
    val uiFolders: List<UiFolder> = emptyList(),
    val uiNotes: List<UiNote> = emptyList(),
)