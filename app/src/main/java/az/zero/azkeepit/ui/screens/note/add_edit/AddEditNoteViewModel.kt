package az.zero.azkeepit.ui.screens.note.add_edit

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import az.zero.azkeepit.data.local.entities.note.DbNote
import az.zero.azkeepit.data.repository.FolderRepository
import az.zero.azkeepit.data.repository.NoteRepository
import az.zero.azkeepit.domain.commons.FOLDER_INITIAL_ID
import az.zero.azkeepit.domain.mappers.UiFolder
import az.zero.azkeepit.domain.mappers.UiNote
import az.zero.azkeepit.ui.composables.ColorPallet.DarkHex
import az.zero.azkeepit.ui.composables.getColorFromHex
import az.zero.azkeepit.util.JDateTimeUtil
import az.zero.azkeepit.util.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class AddEditNoteScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: AddEditNoteScreenArgs = savedStateHandle.toRoute()
    private val isNewNote = args.noteId == null

    private val localUiNote = MutableStateFlow(emptyUiNote)
    private val allFolders = folderRepository.getUiFolders()
    private val shouldPopUp = MutableStateFlow(false)
    private val deleteDialogOpened = MutableStateFlow(false)
    private val selectFolderDialogOpened = MutableStateFlow(false)
    private val isNotePasswordDialogOpened = MutableStateFlow(false)

    val state = combine(
        localUiNote,
        shouldPopUp,
        deleteDialogOpened,
        allFolders,
        selectFolderDialogOpened,
        isNotePasswordDialogOpened
    ) {
            localUiNote, shouldPopUp, isDeleteDialogOpened, allFolders,
            isSelectFolderDialogOpened, isNotePasswordDialogOpened,
        ->
        AddEditNoteState(
            note = localUiNote,
            numberOfWordsForContent = localUiNote.content.length,
            isSaveActive = localUiNote.title.isNotBlank() || localUiNote.content.isNotBlank(),
            allFolders = allFolders,
            isNoteNew = isNewNote,
            shouldPopUp = shouldPopUp,
            isDeleteDialogOpened = isDeleteDialogOpened,
            isSelectFolderDialogOpened = isSelectFolderDialogOpened,
            isNotePasswordDialogOpened = isNotePasswordDialogOpened
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddEditNoteState()
    )

    fun saveNote() = viewModelScope.launch {
        if (!state.value.isSaveActive) {
            shouldPopUp.emit(true)
            return@launch
        }

        val dbNote = DbNote(
            title = state.value.note.title.trim(),
            content = state.value.note.content.trim(),
            isLocked = state.value.note.isLocked,
            createdAt = state.value.note.createdAt,
            images = state.value.note.images.map { it.toString() },
            color = state.value.note.color.toArgb(),
            hashedPassword = state.value.note.password,
            ownerFolderId = state.value.note.ownerUiFolder?.folderId ?: FOLDER_INITIAL_ID,
            noteId = args.noteId
        )

        noteRepository.saveNote(isNewNote = isNewNote, dbNote = dbNote)
        shouldPopUp.emit(true)
    }

    fun changeDeleteDialogOpenState(isOpened: Boolean) = viewModelScope.launch {
        deleteDialogOpened.emit(isOpened)
    }

    fun updateTitle(text: String) = viewModelScope.launch {
        localUiNote.emit(localUiNote.value.copy(title = text))
    }

    fun updateContent(text: String) = viewModelScope.launch {
        localUiNote.emit(localUiNote.value.copy(content = text))
    }

    fun updateIsLocked(isLocked: Boolean, newPassword: String?) = viewModelScope.launch {
        localUiNote.emit(
            localUiNote.value.copy(
                isLocked = isLocked,
                password = newPassword
            )
        )
    }

    fun addNoteToFolder(newUiFolder: UiFolder) = viewModelScope.launch {
        localUiNote.emit(localUiNote.value.copy(ownerUiFolder = newUiFolder))
    }

    fun changeSelectFolderDialogOpenState(isOpened: Boolean) = viewModelScope.launch {
        selectFolderDialogOpened.emit(isOpened)
    }

    fun changeSetPasswordDialogOpened(isOpened: Boolean) = viewModelScope.launch {
        isNotePasswordDialogOpened.emit(isOpened)
    }

    fun updateNoteColor(newColor: Color) = viewModelScope.launch {
        localUiNote.emit(
            localUiNote.value.copy(
                color = newColor
            )
        )
    }

    fun deleteNote() = viewModelScope.launch {
        val noteId = args.noteId ?: return@launch
        val deletedNoteId = noteRepository.deleteNote(noteId = noteId)
        if (deletedNoteId != -1) shouldPopUp.emit(true)
    }

    fun addImages(newImages: List<Uri>) = viewModelScope.launch {
        val oldImages = localUiNote.value.images
        val newList = (oldImages + newImages).distinct()
        localUiNote.emit(localUiNote.value.copy(images = newList))
    }

    fun removeImage(dataUri: Uri) = viewModelScope.launch {
        val oldImages = localUiNote.value.images.toMutableList()
        oldImages.remove(dataUri)
        localUiNote.emit(localUiNote.value.copy(images = oldImages))
    }

    fun removeNoteFromFolder() = viewModelScope.launch {
        localUiNote.emit(localUiNote.value.copy(ownerUiFolder = null))
    }

    init {
        viewModelScope.launch {
            val noteId = args.noteId ?: return@launch
            val uiNote = noteRepository.getNoteById(noteId) ?: return@launch
            localUiNote.emit(uiNote)
        }
    }
}


data class AddEditNoteState(
    val note: UiNote = emptyUiNote,
    val isSaveActive: Boolean = false,
    val numberOfWordsForContent: Int = 0,
    val allFolders: List<UiFolder> = emptyList(),
    val isNoteNew: Boolean = false,
    val shouldPopUp: Boolean = false,
    val isDeleteDialogOpened: Boolean = false,
    val isSelectFolderDialogOpened: Boolean = false,
    val isNotePasswordDialogOpened: Boolean = false,
)

@Serializable
data class AddEditNoteScreenArgs(val noteId: Long?)

private val createdDate = JDateTimeUtil.now()
private val emptyUiNote = UiNote(
    "",
    "",
    false,
    createdDate,
    JDateTimeUtil.toShortDateTimeFormat(createdDate),
    JDateTimeUtil.toLongDateTimeFormat(createdDate),
    emptyList(),
    getColorFromHex(DarkHex),
    null,
    -1L,
    false,
    null
)