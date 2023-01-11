@file:OptIn(ExperimentalPagerApi::class)

package az.zero.azkeepit.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import az.zero.azkeepit.ui.composables.BasicHeaderWithBackBtn
import az.zero.azkeepit.ui.composables.CustomEditText
import az.zero.azkeepit.ui.composables.DrawCircleBorder
import az.zero.azkeepit.ui.composables.TabPager
import az.zero.azkeepit.ui.screens.destinations.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.home.tab_screens.FolderScreen
import az.zero.azkeepit.ui.screens.home.tab_screens.NotesScreen
import az.zero.azkeepit.ui.theme.selectedColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val tabs = listOf("Notes", "Folders")
    var currentTab by remember { mutableStateOf(0) }
    var isCreateFolderDialogOpened by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Red),
        topBar = {
            HomeAppBar(
                onEditClick = {},
                onSearchClick = {},
                onMoreClick = {}
            )
        },
        floatingActionButton = {
            HomeFab(
                currentTab = currentTab,
                onAddNoteClick = {
                    navigator.navigate(AddEditNoteScreenDestination(null))
                },
                onAddFolderClick = {
                    isCreateFolderDialogOpened = true
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            TabPager(
                animateScrollToPage = true,
                tabSelectorHeight = 4.dp,
                tabSelectorColor = selectedColor,
                selectedContentColor = selectedColor,
                tabs = tabs,
                onTabChange = {
                    currentTab = it
                }
            ) {
                when (it) {
                    0 -> NotesScreen(viewModel, navigator)
                    1 -> FolderScreen(viewModel, navigator)
                }
            }

            HomeCustomDialog(openDialog = isCreateFolderDialogOpened,
                onDismiss = { isCreateFolderDialogOpened = false },
                onCreateClick = viewModel::createFolder
            )
        }

    }
}

@Composable
fun HomeFab(
    currentTab: Int,
    onAddNoteClick: () -> Unit,
    onAddFolderClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = if (currentTab == 0) onAddNoteClick else onAddFolderClick,
    ) {
        if (currentTab == 0) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.CreateNewFolder,
                contentDescription = stringResource(id = R.string.add)
            )
        }
    }

}

@Composable
fun HomeAppBar(
    onEditClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
) {

    BasicHeaderWithBackBtn(
        text = stringResource(id = R.string.app_name),
        elevation = 0.dp,
        actions = {
            IconButton(
                onClick = onEditClick
            ) {
                Icon(
                    Icons.Filled.Edit,
                    stringResource(id = R.string.edit),
                    tint = MaterialTheme.colors.onBackground,
                )
            }


            IconButton(
                onClick = onSearchClick
            ) {
                Icon(
                    Icons.Filled.Search,
                    stringResource(id = R.string.search),
                    tint = MaterialTheme.colors.onBackground
                )
            }


            IconButton(
                onClick = onMoreClick
            ) {
                DrawCircleBorder {
                    Icon(
                        Icons.Filled.MoreHoriz,
                        stringResource(id = R.string.more),
                        tint = MaterialTheme.colors.onBackground,
                    )
                }
            }

        }
    )
}

@Composable
fun HomeCustomDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onCreateClick: (playlistName: String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val textBtnColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val isCreateEnabled by remember(text) { mutableStateOf(text.isNotBlank()) }
    val createBtnColor = if (isCreateEnabled) textBtnColor else Color.Gray

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                text = ""
                onDismiss()
            },
            text = {
                CustomEditText(
                    text = text,
                    hint = stringResource(id = R.string.folder_name),
                    modifier = Modifier.fillMaxWidth(),
                    onTextChanged = { text = it }
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        enabled = isCreateEnabled,
                        onClick = {
                            onDismiss()
                            onCreateClick(text.trim())
                            text = ""
                        }
                    ) {
                        Text(stringResource(id = R.string.create), color = createBtnColor)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    TextButton(
                        onClick = {
                            onDismiss()
                            text = ""
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel), color = textBtnColor)
                    }
                }
            }
        )
    }
}



