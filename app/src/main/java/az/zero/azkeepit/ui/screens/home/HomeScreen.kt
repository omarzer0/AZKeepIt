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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.HeaderWithBackBtn
import az.zero.azkeepit.ui.composables.TabPager
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.screens.HomeScreenDestination
import az.zero.azkeepit.ui.screens.folder.details.FolderDetailsScreenArgs
import az.zero.azkeepit.ui.screens.home.tab_screens.folder.FolderScreen
import az.zero.azkeepit.ui.screens.home.tab_screens.note.NotesScreen
import az.zero.azkeepit.ui.theme.selectedColor

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    // TODO 2: move the navArgs to the proper place+
    onFolderClick: (FolderDetailsScreenArgs) -> Unit,
    onNoteClick: (noteId: Long?) -> Unit,
    onNoteWithPasswordClick: (noteId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val tabs = rememberSaveable { listOf("Notes", "Folders") }
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    BackHandler(enabled = state.isEditModeEnabled) {
        if (state.isEditModeEnabled) viewModel.onEditModeChange(false)
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

    LaunchedEffect(key1 = state.isEditModeEnabled) {
        if (!state.isEditModeEnabled) scrollUp = true
    }

    Scaffold(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),

        topBar = {
            HomeAppBar(
                selectedNumber = state.numberOfSelectedItemsForAction,
                isEditModeOn = state.isEditModeEnabled,
                onSearchClick = onSearchClick,
                onClearSelectionClick = { viewModel.onEditModeChange(isActive = false) }
            )
        },
        floatingActionButton = {
            HomeFab(
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
                currentTab = selectedTab,
                isEditModeOn = state.isEditModeEnabled,
                isScrollingUp = isScrollingUp,

//                // TODO 3: Move the Fab inside the tabs
                onAddNoteClick = {},
                onAddFolderClick = {}
//                onAddNoteClick = { onNoteClick(null) },
//                onAddFolderClick = { viewModel.changeCreateFolderDialogState(isOpened = true) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            TabPager(
                tabs = tabs,
                animateScrollToPage = true,
                tabSelectorHeight = 4.dp,
                tabSelectorColor = selectedColor,
                selectedContentColor = selectedColor,
                isEditModeEnabled = state.isEditModeEnabled,
                onTabChange = { selectedTab = it }
            ) {
                when (it) {
                    0 -> NotesScreen(
                        isEditModeEnabled = state.isEditModeEnabled,
                        onNoteClick = onNoteClick,
                        onNoteWithPasswordClick = onNoteWithPasswordClick,
                        onEditModeChange = { isEditModeEnabled ->
                            viewModel.onEditModeChange(isActive = isEditModeEnabled)
                        },
                        onSelectedNotesNumberChange = viewModel::onNumberOfSelectedItemsForActionChange
                    )

                    1 -> FolderScreen(
                        isEditModeOn = state.isEditModeEnabled,
                        onFolderClick = onFolderClick,
                        onEditModeChange = { isEditModeEnabled ->
                            viewModel.onEditModeChange(isActive = isEditModeEnabled)
                        },
                        onSelectedFoldersNumberChange = viewModel::onNumberOfSelectedItemsForActionChange
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
    // TODO 2: move the navArgs to the proper place+
    onFolderClick: (FolderDetailsScreenArgs) -> Unit,
    onNoteClick: (noteId: Long?) -> Unit,
    onNoteWithPasswordClick: (noteId: Long) -> Unit,
) {
    composable<HomeScreenDestination> {
        HomeScreen(
            onSearchClick = onSearchClick,
            onFolderClick = onFolderClick,
            onNoteClick = onNoteClick,
            onNoteWithPasswordClick = onNoteWithPasswordClick
        )
    }
}