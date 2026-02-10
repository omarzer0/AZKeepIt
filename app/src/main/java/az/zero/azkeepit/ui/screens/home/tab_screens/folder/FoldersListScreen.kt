package az.zero.azkeepit.ui.screens.home.tab_screens.folder

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.CustomCreateDialog
import az.zero.azkeepit.ui.composables.DeleteDialog
import az.zero.azkeepit.ui.models.folder.UiFolder
import az.zero.azkeepit.ui.screens.folder.details.FolderDetailsScreenArgs
import az.zero.azkeepit.ui.screens.home.BottomBarItem
import az.zero.azkeepit.ui.screens.items.FolderItem
import az.zero.azkeepit.ui.theme.cardBgColor

@Composable
fun FolderScreen(
    isEditModeOn: Boolean,
    onFolderClick: (FolderDetailsScreenArgs) -> Unit,
    onEditModeChange: (isActive: Boolean) -> Unit,
    onSelectedFoldersNumberChange: (numberOfSelectedFolders: Int) -> Unit,
    viewModel: FoldersListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    CustomCreateDialog(
        openDialog = state.isCreateFolderDialogOpened,
        headerText = stringResource(id = R.string.create_folder),
        onDismiss = { viewModel.changeCreateFolderDialogState(isOpened = false) },
        onCreateClick = viewModel::createFolder
    )

    LaunchedEffect(state.selectedFoldersNumber) {
        onSelectedFoldersNumberChange(state.selectedFoldersNumber)
    }

    when {
        state.isLoading -> Unit
        state.isEmpty -> EmptyFolderScreen()
        else -> SuccessFolderScreen(
            isEditModeOn = isEditModeOn,
            isDeleteFoldersDialogOpen = state.isDeleteFoldersDialogOpen,
            isFolderActionsEnabled = state.isFolderActionsEnabled,
            uiFolders = state.uiFolders,
            onFolderClick = onFolderClick,
            onChangeDeleteFoldersState = viewModel::onChangeDeleteFoldersState,
            deleteSelectedFolders = viewModel::deleteSelectedFolders,
            onChangeEditModeState = onEditModeChange,
            onAddOrRemoveFolderFromSelected = viewModel::onAddOrRemoveFolderFromSelected,
            onClearSelectedFolders = viewModel::onClearSelectedFolders
        )
    }

}


@Composable
private fun SuccessFolderScreen(
    isEditModeOn: Boolean,
    isDeleteFoldersDialogOpen: Boolean,
    isFolderActionsEnabled: Boolean,
    uiFolders: List<UiFolder>,
    onFolderClick: (FolderDetailsScreenArgs) -> Unit,
    onChangeDeleteFoldersState: (isOpen: Boolean) -> Unit,
    deleteSelectedFolders: () -> Unit,
    onChangeEditModeState: (isActive: Boolean) -> Unit,
    onAddOrRemoveFolderFromSelected: (folderId: Long) -> Unit,
    onClearSelectedFolders: () -> Unit
) {

//    BackHandler(enabled = isEditModeOn) {
//        if (isEditModeOn.not()) onClearSelectedFolders()
//    }

    LaunchedEffect(key1 = isEditModeOn) {
        if (isEditModeOn.not()) onClearSelectedFolders()
    }


    DeleteDialog(
        openDialog = isDeleteFoldersDialogOpen,
        text = stringResource(id = R.string.are_you_sure_you_want_to_delete_all_notes_in_this_folder),
        onDismiss = { onChangeDeleteFoldersState(false) },
        onDeleteClick = deleteSelectedFolders
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
            items(uiFolders, key = { it.folderId }) { uiFolder ->
                FolderItem(
                    uiFolder = uiFolder,
                    isEditModeOn = isEditModeOn,
                    onLongClick = {
                        if (!isEditModeOn) {
                            onChangeEditModeState(true)
                            onAddOrRemoveFolderFromSelected(uiFolder.folderId)
                        }
                    },
                    onFolderClick = {
                        if (isEditModeOn) {
                            onAddOrRemoveFolderFromSelected(uiFolder.folderId)
                        } else {
                            val args = FolderDetailsScreenArgs(
                                id = uiFolder.folderId,
                                folderName = uiFolder.name
                            )
                            onFolderClick(args)
                        }

                    }
                )
            }
        }

        FolderBottomActions(
            isEditModeOn = isEditModeOn,
            enabled = isFolderActionsEnabled,
            onDeleteFoldersClick = { onChangeDeleteFoldersState(true) }
        )

    }
}

@Composable
private fun EmptyFolderScreen() {
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
            painter = painterResource(id = R.drawable.no_folder),
            contentDescription = stringResource(id = R.string.no_folders)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.no_folders),
            style = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onBackground)
        )
    }
}

@Composable
fun FolderBottomActions(
    modifier: Modifier = Modifier,
    isEditModeOn: Boolean,
    enabled: Boolean = false,
    onDeleteFoldersClick: () -> Unit,
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
                onClick = onDeleteFoldersClick
            )
        }
    }
}

