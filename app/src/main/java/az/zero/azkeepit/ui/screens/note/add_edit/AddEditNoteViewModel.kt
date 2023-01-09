package az.zero.azkeepit.ui.screens.note.add_edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.ui.screens.navArgs
import az.zero.azkeepit.util.Empty_Ui_Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@HiltViewModel
class AddEditNoteScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: AddEditNoteScreenArgs = savedStateHandle.navArgs()

    private val _note = MutableStateFlow(Empty_Ui_Note)
    val note = _note.asStateFlow()


    init {
        viewModelScope.launch {
            val noteId = args.noteId ?: return@launch
            val dbNote = noteRepository.getNoteById(noteId) ?: return@launch
            _note.emit(dbNote)
        }
    }
}


data class AddEditNoteScreenArgs(
    val noteId: Long?,
)