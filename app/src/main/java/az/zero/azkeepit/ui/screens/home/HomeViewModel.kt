package az.zero.azkeepit.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.NoteWithFolder
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.util.JDateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val notesWithFolderName = noteRepository.getNotesWithFolderName()
    private val folders = noteRepository.getFolders()
    private val isEditModeOn = MutableStateFlow(false)
    private val isCreateFolderDialogOpened = MutableStateFlow(false)
    private val currentTab = MutableStateFlow(0)

    fun createFolder(folderName: String) = viewModelScope.launch {
        noteRepository.insertFolder(Folder(folderName, JDateTimeUtil.now(), null))
    }

    fun changeEditModeState(isActive: Boolean) = viewModelScope.launch {
        isEditModeOn.emit(isActive)
    }

    fun changeCreateFolderDialogState(isOpened: Boolean) = viewModelScope.launch {
        isCreateFolderDialogOpened.emit(isOpened)
    }

    fun changeCurrentTap(newTab: Int) = viewModelScope.launch {
        currentTab.emit(newTab)
    }

    val state = combine(
        notesWithFolderName,
        folders,
        isEditModeOn
    ) { notesWithFolderName, folders, isEditModeOn ->
        HomeUiState(
            notesWithFolderName = notesWithFolderName,
            folders = folders,
            isEditModeOn = isEditModeOn
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeUiState()
    )

}

data class HomeUiState(
    val notesWithFolderName: List<NoteWithFolder> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val isEditModeOn: Boolean = false,
    val isCreateFolderDialogOpened: Boolean = false,
    val currentTab: Int = 0,
)