package az.zero.azkeepit.ui.screens.folder.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.*
import az.zero.azkeepit.ui.screens.destinations.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.items.NoteItem
import az.zero.azkeepit.ui.theme.selectedColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
@Destination(
    navArgsDelegate = FolderDetailsScreenArgs::class
)
fun FolderDetailsScreen(
    viewModel: FolderDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {

    val state by viewModel.folderDetailsState.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
//    val moreActions = getMoreActions()

    BackHandler(
        enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded
    ) {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    LaunchedEffect(state.shouldPopUp) {
        if (state.shouldPopUp) navigator.popBackStack()
    }

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            FolderSheetContent(
                viewModel = viewModel,
                onDismiss = { scope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() } }
            )
        },
        topBar = {
            HeaderWithBackBtn(
                text = state.title,
                elevation = 0.dp,
                onBackPressed = viewModel::onBackPressed,
                actions = {
                    FolderBarActions(onMoreClick = {
                        scope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
                    })
                }
            )
        }
    ) {

        DeleteFolderDialog(
            openDialog = state.deleteFolderDialogOpened,
            onDismiss = { viewModel.changeDeleteFolderDialogState(isOpened = false) },
            onDeleteClick = viewModel::deleteFolder
        )

        DeleteAllNotesDialog(
            openDialog = state.deleteAllNotesDialogOpened,
            onDismiss = { viewModel.changeDeleteAllNotesDialogState(isOpened = false) },
            onDeleteClick = viewModel::deleteAllNotes
        )

        FolderRenameDialog(
            initialText = state.title,
            openDialog = state.renameDialogOpened,
            onDismiss = { viewModel.changeRenameDialogState(isOpened = false) },
            onRenameClick = viewModel::renameFolder
        )

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            items(state.folderWithNotes.uiNotes) { noteWithFolder ->
                NoteItem(
                    uiNote = noteWithFolder,
                    onNoteClick = {
                        navigator.navigate(AddEditNoteScreenDestination(noteId = noteWithFolder.note.noteId))
                    }
                )
            }

        }


    }
}

@Composable
fun getMoreActions(viewModel: FolderDetailsViewModel): List<BottomSheetDateItem> {
    return listOf(
        BottomSheetDateItem(
            title = stringResource(id = R.string.rename),
            onClick = { viewModel.changeRenameDialogState(isOpened = true) }
        ),
        BottomSheetDateItem(
            title = stringResource(id = R.string.delete_folder),
            onClick = { viewModel.changeDeleteFolderDialogState(isOpened = true) }
        ),
        BottomSheetDateItem(
            title = stringResource(id = R.string.delete_all_notes_in_this_folder),
            onClick = { viewModel.changeDeleteAllNotesDialogState(isOpened = true) }
        )
    )
}

@Composable
fun FolderSheetContent(
    viewModel: FolderDetailsViewModel,
    onDismiss: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val bottomSheetHeight = configuration.screenHeightDp.dp / 2
    val items = getMoreActions(viewModel)

    BottomSheetWithItems(
        modifier = Modifier.height(bottomSheetHeight),
        items = items,
        onDismiss = onDismiss,
        header = {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = stringResource(id = R.string.select_action),
                style = MaterialTheme.typography.h2.copy(color = selectedColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Composable
fun FolderBarActions(
    onMoreClick: () -> Unit,
) {
    IconButton(
        onClick = onMoreClick
    ) {
        Icon(
            Icons.Filled.MoreVert,
            stringResource(id = R.string.more),
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier.mirror()
        )
    }
}

@Composable
fun FolderRenameDialog(
    initialText: String = "",
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onRenameClick: (name: String) -> Unit,
) {
    var text by remember(openDialog) { mutableStateOf(initialText) }
    val isStartBtnEnabled by remember(openDialog) { derivedStateOf { text.isNotBlank() } }
    val startBtnColor = if (isStartBtnEnabled) MaterialTheme.colors.onBackground else Color.Gray

    EtDialogWithTwoButtons(
        text = text,
        headerText = stringResource(id = R.string.rename_folder),
        onTextChange = { text = it },
        openDialog = openDialog,
        startBtnText = stringResource(id = R.string.rename),
        onStartBtnClick = { onRenameClick(text) },
        startBtnEnabled = isStartBtnEnabled,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = startBtnColor),
        endBtnText = stringResource(id = R.string.cancel),
        onDismiss = {
            text = ""
            onDismiss()
        }
    )
}

@Composable
fun DeleteFolderDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    TextDialogWithTwoButtons(
        titleText = stringResource(id = R.string.are_you_sure_you_want_to_delete_this_folder),
        openDialog = openDialog,
        startBtnText = stringResource(id = R.string.delete),
        onStartBtnClick = onDeleteClick,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = Color.Red),
        endBtnText = stringResource(id = R.string.cancel),
        onDismiss = onDismiss
    )
}

@Composable
fun DeleteAllNotesDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    TextDialogWithTwoButtons(
        titleText = stringResource(id = R.string.are_you_sure_you_want_to_delete_all_notes_in_this_folder),
        openDialog = openDialog,
        startBtnText = stringResource(id = R.string.delete),
        onStartBtnClick = onDeleteClick,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = Color.Red),
        endBtnText = stringResource(id = R.string.cancel),
        onDismiss = onDismiss
    )
}

