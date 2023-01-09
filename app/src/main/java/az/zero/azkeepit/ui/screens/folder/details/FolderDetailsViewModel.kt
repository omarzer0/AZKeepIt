package az.zero.azkeepit.ui.screens.folder.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.domain.model.UiFolderWithNotes
import az.zero.azkeepit.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@HiltViewModel
class FolderDetailsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val folderDetailsScreenArgs: FolderDetailsScreenArgs = savedStateHandle.navArgs()
    val folderWithNotes = noteRepository.getFolderWithNotesById(folderDetailsScreenArgs.id)

    val emptyUiFolderWithNotes = UiFolderWithNotes(
        -1, "", emptyList()
    )

}

data class FolderDetailsScreenArgs(
    val id: Long,
    val folderName: String,
)