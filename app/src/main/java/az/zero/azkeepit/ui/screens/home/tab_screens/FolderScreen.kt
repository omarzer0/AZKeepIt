package az.zero.azkeepit.ui.screens.home.tab_screens

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.data.local.entities.Folder
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.screens.destinations.FolderDetailsScreenDestination
import az.zero.azkeepit.ui.screens.folder.details.FolderDetailsScreenArgs
import az.zero.azkeepit.ui.screens.home.HomeViewModel
import az.zero.azkeepit.ui.screens.items.FolderItem
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalComposeUiApi
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
        items(folders, key = {it.folderId!!}) { folder ->
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

// LazyColumn(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 16.dp)
//                                .weight(1f),
//                        ) {
//                            itemsIndexed(
//                                items = listItems,
//                                key = { _, item -> item.id },
//                            ) { _, item ->
//                                ListItem(
//                                    label = item.label,
//                                    color = item.color,
//                                    modifier = Modifier
//                                        .animateItemPlacement(
//                                            animationSpec = tween(
//                                                durationMillis = 500,
//                                                easing = LinearOutSlowInEasing,
//                                            )
//                                        )
//                                        .fillMaxWidth()
//                                        .padding(vertical = 8.dp)
//                                        .height(48.dp),
//                                )
//                            }
//                        }

