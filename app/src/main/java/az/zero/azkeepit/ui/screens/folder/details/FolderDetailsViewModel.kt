package az.zero.azkeepit.ui.screens.folder.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.FolderWithNotes
import az.zero.azkeepit.data.local.entities.UiFolderWithUiNotes
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@HiltViewModel
class FolderDetailsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val folderDetailsScreenArgs: FolderDetailsScreenArgs = savedStateHandle.navArgs()
    private val folderId = folderDetailsScreenArgs.id
    private val folderWithNotes = noteRepository.getFolderWithNotesById(folderId)
    private val deleteFolderDialogOpened = MutableStateFlow(false)
    private val deleteAllNotesDialogOpened = MutableStateFlow(false)
    private val renameDialogOpened = MutableStateFlow(false)
    private val shouldPopUp = MutableStateFlow(false)


    val folderDetailsState = combine(
        folderWithNotes,
        deleteFolderDialogOpened,
        deleteAllNotesDialogOpened,
        renameDialogOpened,
        shouldPopUp
    ) { folderWithNotes, deleteFolderDialogOpened, deleteAllNotesDialogOpened, renameDialogOpened, shouldPopUp ->
        FolderDetailsState(
            title = folderWithNotes?.folder?.name ?: folderDetailsScreenArgs.folderName,
            folderWithNotes = folderWithNotes ?: emptyUiFolderWithUiNotes,
            deleteFolderDialogOpened = deleteFolderDialogOpened,
            deleteAllNotesDialogOpened = deleteAllNotesDialogOpened,
            renameDialogOpened = renameDialogOpened,
            shouldPopUp = shouldPopUp
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FolderDetailsState()
    )

    fun changeRenameDialogState(isOpened: Boolean) = viewModelScope.launch {
        renameDialogOpened.emit(isOpened)
    }

    fun changeDeleteFolderDialogState(isOpened: Boolean) = viewModelScope.launch {
        deleteFolderDialogOpened.emit(isOpened)
    }

    fun changeDeleteAllNotesDialogState(isOpened: Boolean) = viewModelScope.launch {
        deleteAllNotesDialogOpened.emit(isOpened)
    }

    fun deleteAllNotes() = viewModelScope.launch {
        noteRepository.deleteAllNotesFromFolder(folderId)
    }


    fun deleteFolder() = viewModelScope.launch {
        noteRepository.deleteFolder(folderId)
        shouldPopUp.emit(true)
    }

    fun onBackPressed() = viewModelScope.launch {
        shouldPopUp.emit(true)
    }

    fun renameFolder(name: String) = viewModelScope.launch {
        noteRepository.renameFolder(folderId = folderId, newName = name)
    }
}

data class FolderDetailsState(
    val title: String = "",
    val folderWithNotes: UiFolderWithUiNotes = emptyUiFolderWithUiNotes,
    val deleteFolderDialogOpened: Boolean = false,
    val deleteAllNotesDialogOpened: Boolean = false,
    val renameDialogOpened: Boolean = false,
    val shouldPopUp: Boolean = false,
)


val emptyUiFolderWithUiNotes = UiFolderWithUiNotes(
    folder = null,
    noteWithFolders = emptyList()
)


val emptyUiFolderWithNotes = FolderWithNotes(
    folder = Folder("", -1L),
    notes = emptyList()
)

data class FolderDetailsScreenArgs(
    val id: Long,
    val folderName: String,
)