package az.zero.azkeepit.ui.screens.details.folder

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.zero.azkeepit.ui.composables.BasicHeaderWithBackBtn
import az.zero.azkeepit.ui.composables.NoteItem
import az.zero.azkeepit.ui.theme.bgColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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
            .background(bgColor)
    ) {
        BasicHeaderWithBackBtn(
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


            items(notes, key = { it.noteId }) { uiNote ->
                NoteItem(
                    uiNote = uiNote,
                    onNoteClick = {

                    }
                )
            }
        }
    }

}

