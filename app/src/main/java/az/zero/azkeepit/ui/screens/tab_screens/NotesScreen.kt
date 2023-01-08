package az.zero.azkeepit.ui.screens.tab_screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.ui.composables.NoteItem
import az.zero.azkeepit.ui.screens.home.HomeViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalFoundationApi
@Composable
fun NotesScreen(
    viewModel: HomeViewModel,
    navigator: DestinationsNavigator,
) {

    val notes by viewModel.notes.collectAsState(initial = emptyList())

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(notes) { uiNote ->
            NoteItem(
                uiNote = uiNote,
                onNoteClick = {

                }
            )
        }
    }
}
