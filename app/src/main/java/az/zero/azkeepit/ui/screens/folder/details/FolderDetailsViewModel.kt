package az.zero.azkeepit.ui.screens.folder.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import az.zero.azkeepit.data.repository.FolderRepository
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.domain.mappers.UiFolder
import az.zero.azkeepit.util.emptyUiFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@HiltViewModel
class FolderDetailsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val folderDetailsScreenArgs = savedStateHandle.toRoute<FolderDetailsScreenArgs>(
//        typeMap = mapOf(typeOf<FolderDetailsScreenArgs>() to serializableNavType<FolderDetailsScreenArgs>())
    )

    private val folderId = folderDetailsScreenArgs.id
    private val uiFolder = folderRepository.getUiFolderById(folderId)
    private val deleteFolderDialogOpened = MutableStateFlow(false)
    private val deleteAllNotesDialogOpened = MutableStateFlow(false)
    private val renameDialogOpened = MutableStateFlow(false)
    private val shouldPopUp = MutableStateFlow(false)


    val folderDetailsState = combine(
        uiFolder,
        deleteFolderDialogOpened,
        deleteAllNotesDialogOpened,
        renameDialogOpened,
        shouldPopUp
    ) { uiFolder, deleteFolderDialogOpened, deleteAllNotesDialogOpened, renameDialogOpened, shouldPopUp ->
        FolderDetailsState(
            title = uiFolder?.name ?: folderDetailsScreenArgs.folderName,
            uiFolder = uiFolder ?: emptyUiFolder,
            deleteFolderDialogOpened = deleteFolderDialogOpened,
            deleteAllNotesDialogOpened = deleteAllNotesDialogOpened,
            renameDialogOpened = renameDialogOpened,
            shouldPopUp = shouldPopUp,
            isUiFolderLoading = false
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
        folderRepository.deleteFolder(folderId)
        shouldPopUp.emit(true)
    }

    fun onBackPressed() = viewModelScope.launch {
        shouldPopUp.emit(true)
    }

    fun renameFolder(name: String) = viewModelScope.launch {
        folderRepository.renameFolder(folderId = folderId, newName = name)
    }
}

data class FolderDetailsState(
    val title: String = "",
    val uiFolder: UiFolder = emptyUiFolder,
    val isUiFolderLoading: Boolean = true,
    val deleteFolderDialogOpened: Boolean = false,
    val deleteAllNotesDialogOpened: Boolean = false,
    val renameDialogOpened: Boolean = false,
    val shouldPopUp: Boolean = false,
)

@Serializable
data class FolderDetailsScreenArgs(
    val id: Long,
    val folderName: String,
)