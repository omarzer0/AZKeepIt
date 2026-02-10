package az.zero.azkeepit.ui.screens.home.tab_screens.note

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.DeleteDialog
import az.zero.azkeepit.ui.composables.RoundTopOnly
import az.zero.azkeepit.ui.composables.SelectFolderBottomSheet
import az.zero.azkeepit.ui.models.folder.UiFolder
import az.zero.azkeepit.ui.models.note.UiNote
import az.zero.azkeepit.ui.screens.home.BottomBarItem
import az.zero.azkeepit.ui.screens.items.NoteItem
import az.zero.azkeepit.ui.theme.cardBgColor
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    isEditModeEnabled: Boolean,
    onNoteClick: (noteId: Long) -> Unit,
    onNoteWithPasswordClick: (noteId: Long) -> Unit,
    onEditModeChange: (isActive: Boolean) -> Unit,
    onSelectedNotesNumberChange: (numberOfSelectedNotes: Int) -> Unit,
    viewModel: NotesListViewModel = hiltViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.numberOfSelectedNotes) {
        onSelectedNotesNumberChange(state.numberOfSelectedNotes)
    }

    when {
        state.isLoading -> Unit
        state.isEmpty -> EmptyNotesScreen()
        else -> {
            SuccessNotesScreen(
                isEditModeEnabled = isEditModeEnabled,
                isDeleteNotesDialogOpen = state.isDeleteNotesDialogOpen,
                isNoteActionsEnabled = state.isNoteActionsEnabled,
                uiFolders = state.uiFolders,
                uiNotes = state.uiNotes,
                onNoteClick = onNoteClick,
                onNoteWithPasswordClick = onNoteWithPasswordClick,
                onDismiss = { viewModel.changeDeleteNotesDialogState(isOpened = false) },
                onDeleteSelectedNotes = { viewModel.changeDeleteNotesDialogState(isOpened = false) },
                onMoveSelectedNotesToFolder = viewModel::onMoveSelectedNotesToFolder,
                onChangeEditModeState = onEditModeChange,
                onAddOrRemoveNoteFromSelected = viewModel::onAddOrRemoveNoteFromSelected,
                onChangeDeleteNotesDialogState = viewModel::onChangeDeleteNotesDialogState,
                onClearSelectedNotes = viewModel::onClearSelectedNotes
            )
        }
    }

}

@Composable
private fun SuccessNotesScreen(
    isEditModeEnabled: Boolean,
    isDeleteNotesDialogOpen: Boolean,
    isNoteActionsEnabled: Boolean,
    uiFolders: List<UiFolder>,
    uiNotes: List<UiNote>,
    onNoteClick: (noteId: Long) -> Unit,
    onNoteWithPasswordClick: (noteId: Long) -> Unit,
    onDismiss: () -> Unit,
    onDeleteSelectedNotes: () -> Unit,
    onMoveSelectedNotesToFolder: (folderId: Long) -> Unit,
    onChangeEditModeState: (isActive: Boolean) -> Unit,
    onAddOrRemoveNoteFromSelected: (noteId: Long) -> Unit,
    onChangeDeleteNotesDialogState: (isBottomActionOpened: Boolean) -> Unit,
    onClearSelectedNotes: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    BackHandler(enabled = bottomState.isVisible) {
        scope.launch { bottomState.hide() }
    }

    LaunchedEffect(key1 = isEditModeEnabled) {
        if (isEditModeEnabled.not()) {
            // TODO(REMOVE) is that needed after refactoring
            scope.launch { bottomState.hide() }
            onClearSelectedNotes()
        }
    }

    DeleteDialog(
        openDialog = isDeleteNotesDialogOpen,
        text = stringResource(id = R.string.are_you_sure_you_want_to_delete_all_selected_notes),
        onDismiss = onDismiss,
        onDeleteClick = onDeleteSelectedNotes
    )

    // TODO(FIXME) wierd animation when long click on a note and mote it to folder
    //  --> the bottom sheet animation is delayed a bit
    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetState = bottomState,
        sheetShape = RoundTopOnly(),
        scrimColor = Color.Transparent,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContent = {
            SelectFolderBottomSheet(
                uiFolders = uiFolders,
                backgroundColor = cardBgColor,
                titleText = stringResource(id = R.string.select_folder),
                onDismiss = { scope.launch { bottomState.hide() } },
                onClick = { onMoveSelectedNotesToFolder(it.folderId) }
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
                items(uiNotes) { uiNote ->
                    NoteItem(
                        uiNote = uiNote,
                        isEditModeEnabled = isEditModeEnabled,
                        onLongClick = {
                            if (!isEditModeEnabled) {
                                onChangeEditModeState(true)
                                onAddOrRemoveNoteFromSelected(uiNote.noteId)
                            }
                        },
                        onNoteClick = {
                            when {
                                isEditModeEnabled -> {
                                    onAddOrRemoveNoteFromSelected(uiNote.noteId)
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

            // TODO(FIXME) fix the delete issue
            NoteBottomActions(
                isEditModeEnabled = isEditModeEnabled,
                enabled = isNoteActionsEnabled,
                onDeleteNotesClick = { onChangeDeleteNotesDialogState(true) },
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
    isEditModeEnabled: Boolean,
    enabled: Boolean = false,
    onDeleteNotesClick: () -> Unit,
    onMoveNotesClick: () -> Unit,
) {
    AnimatedVisibility(visible = isEditModeEnabled) {
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