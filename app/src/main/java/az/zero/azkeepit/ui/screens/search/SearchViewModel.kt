package az.zero.azkeepit.ui.screens.search

import androidx.lifecycle.ViewModel
import az.zero.azkeepit.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {
}