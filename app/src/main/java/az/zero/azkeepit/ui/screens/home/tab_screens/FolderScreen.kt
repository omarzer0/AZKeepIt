package az.zero.azkeepit.ui.screens.home.tab_screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.ui.screens.destinations.FolderDetailsScreenDestination
import az.zero.azkeepit.ui.screens.folder.details.FolderDetailsScreenArgs
import az.zero.azkeepit.ui.screens.home.HomeViewModel
import az.zero.azkeepit.ui.screens.items.FolderItem
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun FolderScreen(
    navigator: DestinationsNavigator,
    folders : List<Folder>,
    isEditModeOn :Boolean
) {

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(folders, key = { it.folderId!! }) { folder ->
            FolderItem(
                folder = folder,
                onFolderClick = {
                    val args = FolderDetailsScreenArgs(
                        id = folder.folderId ?: -1,
                        folderName = folder.name
                    )

                    navigator.navigate(FolderDetailsScreenDestination(navArgs = args))
                }
            )
        }
    }
}