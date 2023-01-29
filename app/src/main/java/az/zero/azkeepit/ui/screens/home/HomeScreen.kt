package az.zero.azkeepit.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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

@OptIn(ExperimentalPagerApi::class)
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
    val tabs = remember(Unit) { listOf("Notes", "Folders") }
    val state by viewModel.state.collectAsState()

    val selectedNumber by remember(state) {
        mutableStateOf(
            if (state.currentTab == 0) state.selectedNotesNumber
            else state.selectedFolderNumber
        )
    }

    BackHandler(enabled = state.isEditModeOn) {
        viewModel.changeEditModeState(isActive = false)
    }

    var scrollUp by remember { mutableStateOf(true) }
    val isScrollingUp by remember { derivedStateOf { scrollUp } }

    // top and right are negative values
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                scrollUp = available.y >= 0
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(key1 = state.isEditModeOn) {
        if (!state.isEditModeOn) scrollUp = true
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),

        topBar = {
            HomeAppBar(
                selectedNumber = selectedNumber,
                isEditModeOn = state.isEditModeOn,
                onSearchClick = { navigator.navigate(SearchScreenDestination()) },
                onClearSelectionClick = { viewModel.changeEditModeState(isActive = false) }
            )
        },
        floatingActionButton = {
            HomeFab(
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
                currentTab = state.currentTab,
                isEditModeOn = state.isEditModeOn,
                isScrollingUp = isScrollingUp,
                onAddNoteClick = { navigator.navigate(AddEditNoteScreenDestination(null)) },
                onAddFolderClick = { viewModel.changeCreateFolderDialogState(isOpened = true) }
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
                isEditModeOn = state.isEditModeOn,
                tabs = tabs,
                onTabChange = viewModel::changeCurrentTap
            ) {
                when (it) {
                    0 -> NotesScreen(navigator = navigator)
                    1 -> FolderScreen(navigator = navigator)
                }
            }
        }

    }
}

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    text: String,
    enabled: Boolean = false,
    tint: Color = if (enabled) MaterialTheme.colors.onBackground else Color.Gray,
    textStyle: TextStyle = MaterialTheme.typography.h3.copy(
        color = if (enabled) MaterialTheme.colors.onBackground else Color.Gray
    ),
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickableSafeClick(
                enabled = enabled,
                onClick = onClick
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint)

        Text(text = text, style = textStyle)
    }

}

@Composable
fun HomeFab(
    modifier: Modifier = Modifier,
    currentTab: Int,
    isEditModeOn: Boolean,
    isScrollingUp: Boolean,
    onAddNoteClick: () -> Unit,
    onAddFolderClick: () -> Unit,
) {
    AnimatedVisibility(visible = !isEditModeOn && isScrollingUp) {
//    AnimatedVisibility(visible = !isEditModeOn) {
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

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    isEditModeOn: Boolean,
    onSearchClick: () -> Unit,
    selectedNumber: Int,
    onClearSelectionClick: () -> Unit,
) {
    // fixme: when hide the appbar the bottom sheet appears for a sec as the layout height changes
//    AnimatedVisibility(visible = isEditModeOn || isScrollingUp) {
        HeaderWithBackBtn(
            modifier = modifier,
            text = stringResource(id = R.string.app_name),
            elevation = 0.dp,
            actions = {
                AnimatedContent(
                    transitionSpec = {
                        fadeIn() + expandHorizontally() with fadeOut() + shrinkHorizontally()
                    },
                    targetState = isEditModeOn
                ) {
                    if (it) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$selectedNumber ${stringResource(id = R.string.selected)}",
                                style = MaterialTheme.typography.h2.copy(color = selectedColor)
                            )

                            IconButton(
                                onClick = onClearSelectionClick
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    stringResource(id = R.string.close),
                                    tint = MaterialTheme.colors.onBackground,
                                )
                            }
                        }
                    } else {
                        IconButton(
                            onClick = onSearchClick
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                stringResource(id = R.string.search),
                                tint = MaterialTheme.colors.onBackground
                            )
                        }
                    }

                }
            }
        )
}
