package az.zero.azkeepit.ui.screens.home.tab_screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.screens.destinations.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.folder.details.DeleteFolderDialog
import az.zero.azkeepit.ui.screens.home.BottomBarItem
import az.zero.azkeepit.ui.screens.home.HomeViewModel
import az.zero.azkeepit.ui.screens.items.NoteItem
import az.zero.azkeepit.ui.theme.cardBgColor
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun NotesScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val state by viewModel.state.collectAsState()

    DeleteFolderDialog(
        openDialog = state.isDeleteNotesDialogOpen,
        onDismiss = { viewModel.changeDeleteNotesDialogState(isOpened = false) },
        onDeleteClick = viewModel::deleteSelectedNotes
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier.weight(1f),
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state.uiNotes) { uiNote ->
                NoteItem(
                    uiNote = uiNote,
                    isEditModeOn = state.isEditModeOn,
                    onLongClick = {
                        if (!state.isEditModeOn) viewModel.changeEditModeState(isActive = true)
                        viewModel.addOrRemoveNoteFromSelected(noteId = uiNote.noteId)
                    },
                    onNoteClick = {
                        if (state.isEditModeOn) {
                            viewModel.addOrRemoveNoteFromSelected(noteId = uiNote.noteId)
                        } else {
                            navigator.navigate(AddEditNoteScreenDestination(noteId = uiNote.noteId))
                        }
                    }
                )
            }
        }

        AnimatedVisibility(visible = state.isEditModeOn) {
            NoteBottomActions(
                enabled = state.isNoteActionsEnabled,
                onDeleteNotesClick = { viewModel.changeDeleteNotesDialogState(isOpened = true) },
                onMoveNotesClick = {}
            )
        }
    }
}

@Composable
fun NoteBottomActions(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onDeleteNotesClick: () -> Unit,
    onMoveNotesClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(cardBgColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomBarItem(
            modifier = Modifier.weight(1f),
            enabled = enabled,
            icon = Icons.Filled.Delete,
            text = stringResource(id = R.string.delete),
            onClick = onDeleteNotesClick
        )
        BottomBarItem(
            modifier = Modifier.weight(1f),
            enabled = enabled,
            icon = Icons.Filled.DriveFileMove,
            text = stringResource(id = R.string.move_to_folder),
            onClick = onMoveNotesClick
        )

    }
}
