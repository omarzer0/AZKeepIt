package az.zero.azkeepit.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.util.JDateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    val notes = noteRepository.getNotes()
    val folders = noteRepository.getFolders()

    fun createFolder(folderName: String) = viewModelScope.launch {
        noteRepository.insertFolder(Folder(folderName, JDateTimeUtil.now(), null))
    }

}