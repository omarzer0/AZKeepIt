package az.zero.azkeepit.ui.screens.unlock_note_dialog

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.ui.screens.UnlockNoteDialogDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnlockNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = savedStateHandle.toRoute<UnlockNoteDialogDestination>()
    private val noteId = args.noteId


    private val _unlockNoteState = MutableStateFlow(UnlockNoteUiState())
    val unlockNoteState = _unlockNoteState.asStateFlow()


    fun onPasswordEntered(enteredPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isEnteredPasswordCorrect = noteRepository.verifyPassword(noteId, enteredPassword)
            Log.d("SHA256PasswordHasher", "isEnteredPasswordCorrect: $isEnteredPasswordCorrect")
            _unlockNoteState.emit(
                _unlockNoteState.value.copy(
                    passwordCheckedSuccessfully = isEnteredPasswordCorrect,
                    isError = !isEnteredPasswordCorrect,
                    errorMessage = "Wrong password",
                    isLoading = false
                )
            )
        }

    }

    fun getNoteId() = noteId

}


data class UnlockNoteUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val passwordCheckedSuccessfully: Boolean = false
)


//
//    private val _unlockNoteState = MutableStateFlow<UnlockNoteUiState>(UnlockNoteUiState.Loading)
//    val unlockNoteState = _unlockNoteState.asStateFlow()
//
//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            val noteUi = noteRepository.getNoteById(noteId)
//            if (noteUi == null || noteUi.password == null) {
//                _unlockNoteState.emit(UnlockNoteUiState.Error)
//                return@launch
//            }
//
//            val readyState = UnlockNoteUiState.Ready(
//                noteId = noteUi.noteId,
//                notePassword = noteUi.password,
//            )
//
//            _unlockNoteState.emit(readyState)
//        }
//    }
//
//    fun onPasswordEntered(enteredPassword: String) {
//        viewModelScope.launch {
//            val state = unlockNoteState.value
//            if (state !is UnlockNoteUiState.Ready) return@launch
//            val notePassword = state.notePassword
//            val currentState = if (enteredPassword == notePassword) UnlockNoteUiState
//                .CorrectPasswordEntered(noteId)
//            else UnlockNoteUiState.WrongPasswordEntered
//
//            _unlockNoteState.emit(currentState)
//        }
//    }
//
//}

//sealed class UnlockNoteUiState {
//    object Loading : UnlockNoteUiState()
//    object Error : UnlockNoteUiState()
//
//    data class Ready(val noteId: Long, val notePassword: String) : UnlockNoteUiState()
//    object WrongPasswordEntered : UnlockNoteUiState()
//    data class CorrectPasswordEntered(val noteId: Long) : UnlockNoteUiState()
//}