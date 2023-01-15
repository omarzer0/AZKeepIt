@file:OptIn(ExperimentalPagerApi::class)

package az.zero.azkeepit.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.*
import az.zero.azkeepit.ui.screens.destinations.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.destinations.SearchScreenDestination
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
        modifier = modifier.fillMaxSize(),
        topBar = {
            HomeAppBar(
                onEditClick = {},
                onSearchClick = { navigator.navigate(SearchScreenDestination()) },
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

            HomeCustomDialog(
                openDialog = isCreateFolderDialogOpened,
                onDismiss = { isCreateFolderDialogOpened = false },
                onCreateClick = viewModel::createFolder
            )
        }

    }
}

@Composable
fun HomeFab(
    modifier: Modifier = Modifier,
    currentTab: Int,
    onAddNoteClick: () -> Unit,
    onAddFolderClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
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
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
) {

    HeaderWithBackBtn(
        modifier = modifier,
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


//    val density = LocalDensity.current
//    val statusBarTop = WindowInsets.statusBars.getTop(density)
//    val toolbarHeight = 80.dp
//    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
//
//    // our offset to collapse toolbar
//    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
//
//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//                val delta = available.y
//                val newOffset = toolbarOffsetHeightPx.value + delta
//                toolbarOffsetHeightPx.value =
//                    newOffset.coerceIn(-(2 * statusBarTop + toolbarHeightPx), 0f)
//                return Offset.Zero
//            }
//        }
//    }
//

//            .nestedScroll(nestedScrollConnection)

//                modifier = Modifier
//                    .height(toolbarHeight)
//                    .offset {
//                        IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt())
//                    },



