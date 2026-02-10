package az.zero.azkeepit.ui.screens.home.tab_screens.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.repository.FolderRepository
import az.zero.azkeepit.ui.models.folder.UiFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersListViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
) : ViewModel() {

    private val uiFolders = folderRepository.getUiFolders()

    private val isCreateFolderDialogOpened = MutableStateFlow(false)
    private val isDeleteFoldersDialogOpen = MutableStateFlow(false)
    private val selectedFolders = MutableStateFlow(mutableListOf<Long>())

    fun createFolder(folderName: String) {
        viewModelScope.launch {
            folderRepository.createNewFolder(folderName)
        }
    }

    fun changeCreateFolderDialogState(isOpened: Boolean) {
        viewModelScope.launch {
            isCreateFolderDialogOpened.emit(isOpened)
        }
    }

    fun deleteSelectedFolders() = viewModelScope.launch {
        folderRepository.deleteSelectedFolders(selectedFolders.value)
        selectedFolders.emit(mutableListOf())
    }

    fun onChangeDeleteFoldersState(isOpen: Boolean) {
        viewModelScope.launch {
            isDeleteFoldersDialogOpen.emit(isOpen)
        }
    }

    fun onAddOrRemoveFolderFromSelected(folderId: Long?) = viewModelScope.launch {
        if (folderId == null) return@launch
        val folders = selectedFolders.value.toMutableList()
        val exist = folders.any { folderId == it }
        if (exist) folders.remove(folderId)
        else folders.add(folderId)
        selectedFolders.emit(folders)
    }

    fun onClearSelectedFolders() {
        viewModelScope.launch {
            selectedFolders.emit(mutableListOf())
        }
    }

    val state = combine(
        uiFolders,
        isCreateFolderDialogOpened,
        isDeleteFoldersDialogOpen,
        selectedFolders,
    )
    { uiFolders,
      isCreateFolderDialogOpened, isDeleteFoldersDialogOpen, selectedFolders ->
        FoldersListState(
            isEmpty = uiFolders.isEmpty(),
            isLoading = false,
            selectedFoldersNumber = selectedFolders.size,
            isCreateFolderDialogOpened = isCreateFolderDialogOpened,
            isDeleteFoldersDialogOpen = isDeleteFoldersDialogOpen,
            isFolderActionsEnabled = selectedFolders.isNotEmpty(),
            uiFolders = uiFolders.map { it.copy(isSelected = selectedFolders.contains(it.folderId)) },
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FoldersListState()
    )
}

data class FoldersListState(
    val isEmpty: Boolean = false,
    val isLoading: Boolean = true,
    val isCreateFolderDialogOpened: Boolean = false,
    val isDeleteFoldersDialogOpen: Boolean = false,
    val isFolderActionsEnabled: Boolean = false,
    val selectedFoldersNumber: Int = 0,
    val uiFolders: List<UiFolder> = emptyList(),
)