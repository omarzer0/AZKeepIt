package az.zero.azkeepit.ui.screens.home

import androidx.lifecycle.ViewModel
import az.zero.azkeepit.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    val notes = noteRepository.getNotes()
    val folders = noteRepository.getFolders()


}