package az.zero.azkeepit.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.HeaderWithBackBtn
import az.zero.azkeepit.ui.composables.TabPager
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.screens.HomeScreenDestination
import az.zero.azkeepit.ui.screens.folder.details.FolderDetailsScreenArgs
import az.zero.azkeepit.ui.screens.home.tab_screens.FolderScreen
import az.zero.azkeepit.ui.screens.home.tab_screens.NotesScreen
import az.zero.azkeepit.ui.theme.selectedColor

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onNavigateToAddEditNoteScreen: (noteId: Long?) -> Unit,
    // TODO 2: move the navArgs to the proper place+
    onNavigateToFolderDetailsScreen: (FolderDetailsScreenArgs) -> Unit,
    modifier: Modifier = Modifier,
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
            .systemBarsPadding()
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),

        topBar = {
            HomeAppBar(
                selectedNumber = selectedNumber,
                isEditModeOn = state.isEditModeOn,
                onSearchClick = onSearchClick,
                onClearSelectionClick = { viewModel.changeEditModeState(isActive = false) }
            )
        },
        floatingActionButton = {
            HomeFab(
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
                currentTab = state.currentTab,
                isEditModeOn = state.isEditModeOn,
                isScrollingUp = isScrollingUp,
                onAddNoteClick = { onNavigateToAddEditNoteScreen(null) },

                // TODO 3: Update this
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
                    0 -> NotesScreen(
                        onNavigateToAddEditNoteScreen = onNavigateToAddEditNoteScreen,
                    )

                    1 -> FolderScreen(
                        onNavigateToFolderDetailsScreen = onNavigateToFolderDetailsScreen,
                    )
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
            tint = tint
        )

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

@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    isEditModeOn: Boolean,
    onSearchClick: () -> Unit,
    selectedNumber: Int,
    onClearSelectionClick: () -> Unit,
) {
    HeaderWithBackBtn(
        modifier = modifier,
        text = stringResource(id = R.string.app_name),
        elevation = 0.dp,
        actions = {
            AnimatedContent(
                targetState = isEditModeOn,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(
                                initialScale = 0.92f,
                                animationSpec = tween(220, delayMillis = 90)
                            ))
                        .togetherWith(fadeOut(animationSpec = tween(90)))
                }
            ) { editMode ->
                if (editMode) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$selectedNumber ${stringResource(id = R.string.selected)}",
                            style = MaterialTheme.typography.h2.copy(color = selectedColor)
                        )
                        IconButton(onClick = onClearSelectionClick) {
                            Icon(
                                Icons.Filled.Close,
                                stringResource(id = R.string.close),
                                tint = MaterialTheme.colors.onBackground,
                            )
                        }
                    }
                } else {
                    IconButton(onClick = onSearchClick) {
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

internal fun NavGraphBuilder.homeScreen(
    onSearchClick: () -> Unit,
    onNavigateToAddEditNoteScreen: (noteId: Long?) -> Unit,
    // TODO 2: move the navArgs to the proper place+
    onNavigateToFolderDetailsScreen: (FolderDetailsScreenArgs) -> Unit,
) {
    composable<HomeScreenDestination> {
        HomeScreen(
            onSearchClick = onSearchClick,
            onNavigateToAddEditNoteScreen = onNavigateToAddEditNoteScreen,
            onNavigateToFolderDetailsScreen = onNavigateToFolderDetailsScreen
        )
    }
}