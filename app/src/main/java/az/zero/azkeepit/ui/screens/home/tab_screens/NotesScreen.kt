package az.zero.azkeepit.ui.screens.home.tab_screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.DeleteDialog
import az.zero.azkeepit.ui.composables.RoundTopOnly
import az.zero.azkeepit.ui.composables.SelectFolderBottomSheet
import az.zero.azkeepit.ui.screens.home.BottomBarItem
import az.zero.azkeepit.ui.screens.home.HomeUiState
import az.zero.azkeepit.ui.screens.home.HomeViewModel
import az.zero.azkeepit.ui.screens.items.NoteItem
import az.zero.azkeepit.ui.theme.cardBgColor
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    onNoteClick: (noteId: Long) -> Unit,
    onNoteWithPasswordClick: (noteId: Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    // TODO(Improve) try split the 2 states from home screen to noteScreenState and folderScreenState
    val state by viewModel.state.collectAsState()

    when {
        state.isNotesLoading -> Unit
        state.uiNotes.isEmpty() -> EmptyNotesScreen()
        else -> {
            SuccessNotesScreen(
                onNoteClick = onNoteClick,
                onNoteWithPasswordClick = onNoteWithPasswordClick,
                state = state,
                viewModel = viewModel
            )
        }
    }

}

@Composable
private fun SuccessNotesScreen(
    state: HomeUiState,
    onNoteClick: (noteId: Long) -> Unit,
    onNoteWithPasswordClick: (noteId: Long) -> Unit,
    viewModel: HomeViewModel,
) {
    val scope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    BackHandler(enabled = bottomState.isVisible) {
        scope.launch { bottomState.hide() }
    }

    LaunchedEffect(key1 = state.isEditModeOn) {
        if (!state.isEditModeOn) scope.launch { bottomState.hide() }
    }

    DeleteDialog(
        openDialog = state.isDeleteNotesDialogOpen,
        text = stringResource(id = R.string.are_you_sure_you_want_to_delete_all_selected_notes),
        onDismiss = { viewModel.changeDeleteNotesDialogState(isOpened = false) },
        onDeleteClick = viewModel::deleteSelectedNotes
    )

    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetState = bottomState,
        sheetShape = RoundTopOnly(),
        scrimColor = Color.Transparent,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContent = {
            SelectFolderBottomSheet(
                uiFolders = state.uiFolders,
                backgroundColor = cardBgColor,
                titleText = stringResource(id = R.string.select_folder),
                onDismiss = { scope.launch { bottomState.hide() } },
                onClick = { viewModel.moveSelectedNotesToFolder(it.folderId) }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.weight(1f),
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(state.uiNotes) { uiNote ->
                    NoteItem(
                        uiNote = uiNote,
                        isEditModeOn = state.isEditModeOn,
                        onLongClick = {
                            if (!state.isEditModeOn) {
                                viewModel.changeEditModeState(isActive = true)
                                viewModel.addOrRemoveNoteFromSelected(noteId = uiNote.noteId)
                            }
                        },
                        onNoteClick = {
                            when {
                                state.isEditModeOn -> {
                                    viewModel.addOrRemoveNoteFromSelected(noteId = uiNote.noteId)
                                }

                                uiNote.isLocked -> onNoteWithPasswordClick(uiNote.noteId)

                                else -> {
                                    onNoteClick(uiNote.noteId)
                                }
                            }
                        }
                    )
                }
            }

            NoteBottomActions(
                isEditModeOn = state.isEditModeOn,
                enabled = state.isNoteActionsEnabled,
                onDeleteNotesClick = { viewModel.changeDeleteNotesDialogState(isOpened = true) },
                onMoveNotesClick = { scope.launch { bottomState.show() } }
            )
        }
    }
}

@Composable
private fun EmptyNotesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(150.dp),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.no_note),
            contentDescription = stringResource(id = R.string.no_note)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.no_note),
            style = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onBackground)
        )
    }
}

@Composable
fun NoteBottomActions(
    modifier: Modifier = Modifier,
    isEditModeOn: Boolean,
    enabled: Boolean = false,
    onDeleteNotesClick: () -> Unit,
    onMoveNotesClick: () -> Unit,
) {
    AnimatedVisibility(visible = isEditModeOn) {
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

}