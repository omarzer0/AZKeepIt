package az.zero.azkeepit.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.local.entities.folder.DbFolder
import az.zero.azkeepit.data.repository.FolderRepository
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.domain.mappers.UiFolder
import az.zero.azkeepit.domain.mappers.UiNote
import az.zero.azkeepit.util.JDateTimeUtil
import az.zero.azkeepit.util.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
) : ViewModel() {

    private val uiNotes = noteRepository.getUiNotes()
    private val uiFolders = folderRepository.getUiFolders()
    private val isEditModeOn = MutableStateFlow(false)
    private val isDeleteNotesDialogOpened = MutableStateFlow(false)
    private val isCreateFolderDialogOpened = MutableStateFlow(false)
    private val isDeleteFoldersDialogOpen = MutableStateFlow(false)
    private val currentTab = MutableStateFlow(0)
    private val selectedNotes = MutableStateFlow(mutableListOf<Long>())
    private val selectedFolders = MutableStateFlow(mutableListOf<Long>())

    fun createFolder(folderName: String) = viewModelScope.launch {
        folderRepository.insertFolder(DbFolder(folderName, JDateTimeUtil.now(), null))
    }

    fun changeEditModeState(
        isActive: Boolean,
        clearSelection: Boolean = !isActive,
    ) = viewModelScope.launch {
        isEditModeOn.emit(isActive)
        if (clearSelection) postAction()
    }

    fun changeCreateFolderDialogState(isOpened: Boolean) = viewModelScope.launch {
        isCreateFolderDialogOpened.emit(isOpened)
    }

    fun changeCurrentTap(newTab: Int) = viewModelScope.launch {
        currentTab.emit(newTab)
    }

    fun addOrRemoveNoteFromSelected(noteId: Long?) = viewModelScope.launch {
        if (noteId == null) return@launch
        val notes = selectedNotes.value.toMutableList()
        val exist = notes.any { noteId == it }
        if (exist) notes.remove(noteId)
        else notes.add(noteId)
        selectedNotes.emit(notes)
    }

    fun deleteSelectedNotes() = viewModelScope.launch {
        noteRepository.deleteAllSelectedNotes(selectedNotes.value)
        postAction()
    }

    fun moveSelectedNotesToFolder(folderId: Long) = viewModelScope.launch {
        noteRepository.moveNotesToFolder(
            folderId = folderId,
            selectedNotesIds = selectedNotes.value
        )
        postAction()
    }

    fun deleteSelectedFolders() = viewModelScope.launch {
        folderRepository.deleteSelectedFolders(selectedFolders.value)
        postAction()
    }

    private suspend fun postAction() {
        selectedNotes.emit(mutableListOf())
        selectedFolders.emit(mutableListOf())
        isEditModeOn.emit(false)
    }

    fun addOrRemoveFolderFromSelected(folderId: Long?) = viewModelScope.launch {
        if (folderId == null) return@launch
        val folders = selectedFolders.value.toMutableList()
        val exist = folders.any { folderId == it }
        if (exist) folders.remove(folderId)
        else folders.add(folderId)
        selectedFolders.emit(folders)
    }

    fun changeDeleteNotesDialogState(isOpened: Boolean) = viewModelScope.launch {
        isDeleteNotesDialogOpened.emit(isOpened)
    }

    fun changeDeleteFoldersState(isOpened: Boolean) = viewModelScope.launch {
        isDeleteFoldersDialogOpen.emit(isOpened)
    }

    val state = combine(
        uiNotes,
        uiFolders,
        isEditModeOn,
        currentTab,
        isCreateFolderDialogOpened,
        selectedNotes,
        selectedFolders,
        isDeleteNotesDialogOpened,
        isDeleteFoldersDialogOpen,
    )
    {
            uiNotes, uiFolders, isEditModeOn, currentTab, isCreateFolderDialogOpened,
            selectedNotes, selectedFolders, isDeleteNotesDialogOpened, isDeleteFoldersDialogOpen,
        ->
        HomeUiState(
            uiNotes = uiNotes.map { it.copy(isSelected = selectedNotes.contains(it.noteId)) },
            isNotesLoading = false,
            uiFolders = uiFolders.map { it.copy(isSelected = selectedFolders.contains(it.folderId)) },
            isFoldersLoading = false,
            isEditModeOn = isEditModeOn,
            isNoteActionsEnabled = selectedNotes.isNotEmpty(),
            isFolderActionsEnabled = selectedFolders.isNotEmpty(),
            currentTab = currentTab,
            isCreateFolderDialogOpened = isCreateFolderDialogOpened,
            selectedNotesNumber = selectedNotes.size,
            selectedFolderNumber = selectedFolders.size,
            isDeleteNotesDialogOpen = isDeleteNotesDialogOpened,
            isDeleteFoldersDialogOpen = isDeleteFoldersDialogOpen,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeUiState()
    )

}

data class HomeUiState(
    val uiNotes: List<UiNote> = emptyList(),
    val isNotesLoading: Boolean = true,
    val uiFolders: List<UiFolder> = emptyList(),
    val isFoldersLoading: Boolean = true,
    val isEditModeOn: Boolean = false,
    val isNoteActionsEnabled: Boolean = false,
    val selectedNotesNumber: Int = 0,
    val isFolderActionsEnabled: Boolean = false,
    val selectedFolderNumber: Int = 0,
    val isCreateFolderDialogOpened: Boolean = false,
    val currentTab: Int = 0,
    val isDeleteNotesDialogOpen: Boolean = false,
    val isDeleteFoldersDialogOpen: Boolean = false,
)