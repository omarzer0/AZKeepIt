package az.zero.azkeepit.ui.screens.home.tab_screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.EtDialogWithTwoButtons
import az.zero.azkeepit.ui.screens.destinations.FolderDetailsScreenDestination
import az.zero.azkeepit.ui.screens.folder.details.FolderDetailsScreenArgs
import az.zero.azkeepit.ui.screens.home.BottomBarItem
import az.zero.azkeepit.ui.screens.home.HomeViewModel
import az.zero.azkeepit.ui.screens.items.FolderItem
import az.zero.azkeepit.ui.theme.cardBgColor
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun FolderScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val state by viewModel.state.collectAsState()

    CreateFolderDialog(
        openDialog = state.isCreateFolderDialogOpened,
        onDismiss = { viewModel.changeCreateFolderDialogState(isOpened = false) },
        onCreateClick = viewModel::createFolder
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .weight(1f),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state.uiFolders, key = { it.folderId }) { uiFolder ->
                FolderItem(
                    uiFolder = uiFolder,
                    isEditModeOn = state.isEditModeOn,
                    onLongClick = {
                        if (!state.isEditModeOn) viewModel.changeEditModeState(isActive = true)
                        viewModel.addOrRemoveFolderFromSelected(folderId = uiFolder.folderId)
                    },
                    onFolderClick = {
                        if (state.isEditModeOn) {
                            viewModel.addOrRemoveFolderFromSelected(folderId = uiFolder.folderId)
                        } else {
                            val args = FolderDetailsScreenArgs(id = uiFolder.folderId,
                                folderName = uiFolder.name)
                            navigator.navigate(FolderDetailsScreenDestination(navArgs = args))
                        }

                    }
                )
            }
        }

        AnimatedVisibility(visible = state.isEditModeOn) {
            FolderBottomActions(
                enabled = state.isFolderActionsEnabled,
                onDeleteFoldersClick = { }
            )
        }
    }
}

@Composable
fun FolderBottomActions(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onDeleteFoldersClick: () -> Unit,
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
            onClick = onDeleteFoldersClick
        )
    }
}

@Composable
fun CreateFolderDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onCreateClick: (name: String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val isStartBtnEnabled by remember { derivedStateOf { text.isNotBlank() } }
    val startBtnColor = if (isStartBtnEnabled) MaterialTheme.colors.onBackground else Color.Gray

    EtDialogWithTwoButtons(
        text = text,
        headerText = stringResource(id = R.string.create_folder),
        onTextChange = { text = it },
        openDialog = openDialog,
        startBtnText = stringResource(id = R.string.create),
        onStartBtnClick = { onCreateClick(text) },
        startBtnEnabled = isStartBtnEnabled,
        startBtnStyle = MaterialTheme.typography.h3.copy(color = startBtnColor),
        endBtnText = stringResource(id = R.string.cancel),
        onDismiss = {
            text = ""
            onDismiss()
        }
    )
}