package az.zero.azkeepit.ui.screens.add_edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalFoundationApi
@HiltViewModel
class FolderDetailsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val folderDetailsScreenArgs: AddEditNoteScreenArgs = savedStateHandle.navArgs()

}


data class AddEditNoteScreenArgs(
    val noteId: Long?,
)