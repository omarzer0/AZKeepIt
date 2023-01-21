package az.zero.azkeepit.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    var query = MutableStateFlow("")
        private set

    val searchedUiNotes = query.flatMapLatest {
        noteRepository.searchNotes(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )


    fun searchNotes(newQuery: String) = viewModelScope.launch {
        query.emit(newQuery)
    }
}