@file:OptIn(ExperimentalPagerApi::class)

package az.zero.azkeepit.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.BasicHeaderWithBackBtn
import az.zero.azkeepit.ui.composables.DrawCircleBorder
import az.zero.azkeepit.ui.composables.TabPager
import az.zero.azkeepit.ui.screens.note.add_edit.AddEditNoteScreenArgs
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
            HomeFab(onClick = {
                navigator.navigate(AddEditNoteScreenDestination(null))
            })
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
                tabs = tabs
            ) {
                when (it) {
                    0 -> NotesScreen(viewModel, navigator)
                    1 -> FolderScreen(viewModel, navigator)
                }
            }
        }

    }
}

@Composable
fun HomeFab(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add)
        )
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


