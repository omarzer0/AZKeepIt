package az.zero.azkeepit.ui.screens.tab_screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.domain.model.UiFolder
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.screens.destinations.FolderDetailsScreenDestination
import az.zero.azkeepit.ui.screens.details.folder.FolderDetailsScreenArgs
import az.zero.azkeepit.ui.screens.home.HomeViewModel
import az.zero.azkeepit.ui.theme.cardBgColor
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalFoundationApi
@Composable
fun FolderScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val folders by viewModel.folders.collectAsState(emptyList())

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(folders, key = { it.folderId }) { uiFolder ->
            FolderItem(
                uiFolder = uiFolder,
                onFolderClick = {
                    val args = FolderDetailsScreenArgs(
                        id = uiFolder.folderId,
                        folderName = uiFolder.folderName
                    )

                    navigator.navigate(FolderDetailsScreenDestination(navArgs = args))
                }
            )
        }
    }
}

@Composable
fun FolderItem(
    modifier: Modifier = Modifier,
    uiFolder: UiFolder,
    onFolderClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickableSafeClick(onClick = onFolderClick),
        backgroundColor = cardBgColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.folder),
                contentDescription = stringResource(R.string.folder)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = uiFolder.folderName)
        }
    }
}