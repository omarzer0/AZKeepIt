package az.zero.azkeepit.ui.screens.folder.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.ui.composables.HeaderWithBackBtn
import az.zero.azkeepit.ui.screens.destinations.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.items.NoteItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
@Destination(
    navArgsDelegate = FolderDetailsScreenArgs::class
)
fun FolderDetailsScreen(
    viewModel: FolderDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {

    val folderWithNotes by viewModel.folderWithNotes.collectAsState(initial = viewModel.emptyUiFolderWithNotes)
    val notes = folderWithNotes.notes
    val appBarTitle = viewModel.folderDetailsScreenArgs.folderName

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        HeaderWithBackBtn(
            text = appBarTitle,
            elevation = 0.dp,
            onBackPressed = { navigator.popBackStack() }
        )

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .weight(1f),
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            items(notes) { uiNote ->
                NoteItem(
                    note = uiNote,
                    onNoteClick = {
                        navigator.navigate(AddEditNoteScreenDestination(noteId = uiNote.noteId))
                    }
                )
            }

        }
    }

}

