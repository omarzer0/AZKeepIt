package az.zero.azkeepit.ui.screens.unlock_note_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import az.zero.azkeepit.ui.composables.EnterNotePasswordDialog
import az.zero.azkeepit.ui.screens.UnlockNoteDialogDestination

@Composable
fun UnlockNoteDialogScreen(
    onPasswordCorrect: (noteId: Long) -> Unit,
    onDismiss: () -> Unit,
    viewModel: UnlockNoteViewModel = hiltViewModel()
) {

    val state = viewModel.unlockNoteState.collectAsStateWithLifecycle().value

    LaunchedEffect(state.passwordCheckedSuccessfully) {
        if (state.passwordCheckedSuccessfully) onPasswordCorrect(viewModel.getNoteId())
    }

    EnterNotePasswordDialog(
        isError = state.isError,
        onEnterBtnClick = { enteredPassword ->
            viewModel.onPasswordEntered(enteredPassword)
        },
        onDismiss = onDismiss
    )


}




internal fun NavGraphBuilder.unlockNoteDialog(
    onPasswordCorrect: (noteId: Long) -> Unit,
    onDismiss: () -> Unit,
) {
    dialog<UnlockNoteDialogDestination> {
        UnlockNoteDialogScreen(
            onPasswordCorrect = onPasswordCorrect,
            onDismiss = onDismiss
        )
    }
}