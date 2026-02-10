package az.zero.azkeepit.ui.screens.folder.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.BottomSheetDateItem
import az.zero.azkeepit.ui.composables.BottomSheetWithItems
import az.zero.azkeepit.ui.composables.DeleteDialog
import az.zero.azkeepit.ui.composables.EtDialogWithTwoButtons
import az.zero.azkeepit.ui.composables.HeaderWithBackBtn
import az.zero.azkeepit.ui.composables.RoundTopOnly
import az.zero.azkeepit.ui.composables.mirror
import az.zero.azkeepit.ui.models.note.UiNote
import az.zero.azkeepit.ui.screens.FolderDetailsScreenDestination
import az.zero.azkeepit.ui.screens.items.NoteItem
import az.zero.azkeepit.ui.screens.uriSafeSerializableNavType
import az.zero.azkeepit.ui.theme.cardBgColor
import az.zero.azkeepit.ui.theme.selectedColor
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

// TODO(FEATURE) Add the same long click functionality to the folder detail screen notes
// TODO(FIXME) The bottom sheet header is visible without any action and that shouldn't be the case
@Composable
fun FolderDetailsScreen(
    onBackPressed: () -> Unit,
    onNoteClick: (noteId: Long) -> Unit,
    viewModel: FolderDetailsViewModel = hiltViewModel(),
) {

    val state by viewModel.folderDetailsState.collectAsState()
    val scope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    BackHandler(enabled = bottomState.isVisible) {
        scope.launch { bottomState.hide() }
    }

    LaunchedEffect(state.shouldPopUp) {
        if (state.shouldPopUp) onBackPressed()
    }

    DeleteDialog(
        openDialog = state.deleteFolderDialogOpened,
        text = stringResource(id = R.string.are_you_sure_you_want_to_delete_this_folder),
        onDismiss = { viewModel.changeDeleteFolderDialogState(isOpened = false) },
        onDeleteClick = viewModel::deleteFolder
    )

    DeleteDialog(
        openDialog = state.deleteAllNotesDialogOpened,
        text = stringResource(id = R.string.are_you_sure_you_want_to_delete_all_notes_in_this_folder),
        onDismiss = { viewModel.changeDeleteAllNotesDialogState(isOpened = false) },
        onDeleteClick = viewModel::deleteAllNotes
    )

    FolderRenameDialog(
        initialText = state.title,
        openDialog = state.renameDialogOpened,
        onDismiss = { viewModel.changeRenameDialogState(isOpened = false) },
        onRenameClick = viewModel::renameFolder
    )

    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetState = bottomState,
        sheetShape = RoundTopOnly(),
        scrimColor = Color.Transparent,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContent = {
            FolderSheetContent(
                viewModel = viewModel,
                onDismiss = { scope.launch { bottomState.hide() } }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                HeaderWithBackBtn(
                    text = state.title,
                    elevation = 0.dp,
                    onBackPressed = viewModel::onBackPressed,
                    actions = { FolderBarActions(onMoreClick = { scope.launch { bottomState.show() } }) }
                )
            }
        ) { paddingValues ->
            when {
                state.isUiFolderLoading -> {}
                state.uiFolderWithNotes.folderNotes.isEmpty() -> EmptyFolderDetailsScreen()
                else -> SuccessFolderDetailsScreen(
                    paddingValues = paddingValues,
                    folderNotes = state.uiFolderWithNotes.folderNotes,
                    onNoteClick = onNoteClick
                )
            }
        }
    }
}

@Composable
private fun SuccessFolderDetailsScreen(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    folderNotes: List<UiNote>,
    onNoteClick: (noteId: Long) -> Unit,
) {

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        items(folderNotes) { uiNote ->
            NoteItem(
                uiNote = uiNote,
                isEditModeEnabled = false,
                onNoteClick = onNoteClick
            )
        }

    }
}

@Composable
private fun EmptyFolderDetailsScreen() {
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
        backgroundColor = cardBgColor,
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
        openDialog = openDialog,
        startBtnText = stringResource(id = R.string.rename),
        endBtnText = stringResource(id = R.string.cancel),
        onTextChange = { text = it },
        startBtnEnabled = isStartBtnEnabled,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = startBtnColor),
        onEnterBtnClick = { onRenameClick(text) },
        onDismiss = {
            text = ""
            onDismiss()
        },
    )
}

// TODO remove this if needed
//object FolderDetailsScreenNavTypes {
//    val typeMap = mapOf(
//        typeOf<FolderDetailsScreenArgs>() to serializableNavType<FolderDetailsScreenArgs>()
//    )
//}

@Serializable
data class FolderDetailsScreenArgs(
    val id: Long,
    val folderName: String,
) {
    companion object {
        val typeMap = mapOf(
            typeOf<FolderDetailsScreenArgs>() to uriSafeSerializableNavType<FolderDetailsScreenArgs>()
        )

        fun from(savedStateHandle: SavedStateHandle): FolderDetailsScreenDestination {
            return savedStateHandle.toRoute(typeMap)
        }
    }
}

internal fun NavGraphBuilder.folderDetailsScreen(
    onBackPressed: () -> Unit,
    onNoteClick: (noteId: Long) -> Unit,
) {
    composable<FolderDetailsScreenDestination>(
        typeMap = FolderDetailsScreenDestination.typeMap
    ) {
        FolderDetailsScreen(
            onBackPressed = onBackPressed,
            onNoteClick = onNoteClick
        )
    }
}