package az.zero.azkeepit.ui.screens.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.zero.azkeepit.R
import az.zero.azkeepit.domain.mappers.UiNote
import az.zero.azkeepit.ui.composables.BasicHeaderWithBackBtn
import az.zero.azkeepit.ui.composables.TextWithClearIcon
import az.zero.azkeepit.ui.screens.SearchScreenDestination
import az.zero.azkeepit.ui.screens.items.NoteItem

@Composable
fun SearchScreen(
    onBackPressed: () -> Unit,
    onNoteClick: (noteId: Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val searchedUiNotes by viewModel.searchedUiNotes.collectAsState()
    val searchQuery by viewModel.query.collectAsState()

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        SearchHeader(
            text = searchQuery,
            onTextValueChanged = viewModel::searchNotes,
            onClearClick = { viewModel.searchNotes("") },
            onBackPressed = {
                focusManager.clearFocus()
                onBackPressed()
            }
        )

        if (searchedUiNotes.isEmpty()) {
            EmptySearchScreen()
        } else {
            SuccessSearchScreen(
                searchedUiNotes = searchedUiNotes,
                onNoteClick = onNoteClick
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SuccessSearchScreen(
    searchedUiNotes: List<UiNote>,
    onNoteClick: (noteId: Long) -> Unit,
) {

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        items(searchedUiNotes) {
            NoteItem(
                uiNote = it,
                isEditModeOn = false,
                onNoteClick = onNoteClick
            )
        }
    }


}

@Composable
private fun EmptySearchScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .width(200.dp)
                .height(180.dp)
                .padding(end = 24.dp),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.no_search_found),
            contentDescription = stringResource(id = R.string.no_result_found)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.no_result_found),
            style = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onBackground)
        )
    }
}

@Composable
fun SearchHeader(
    text: String,
    onTextValueChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onClearClick: () -> Unit,
) {

    BasicHeaderWithBackBtn(
        onBackPressed = onBackPressed,
        textContent = {
            TextWithClearIcon(
                modifier = Modifier.background(MaterialTheme.colors.background),
                text = text,
                hint = stringResource(id = R.string.search),
                onClearClick = onClearClick,
                onTextValueChanged = onTextValueChanged
            )
        }
    )
}

internal fun NavGraphBuilder.searchScreen(
    onBackPressed: () -> Unit,
    onNoteClick: (noteId: Long) -> Unit,
) {
    composable<SearchScreenDestination> {
        SearchScreen(
            onBackPressed = onBackPressed,
            onNoteClick = onNoteClick
        )
    }
}