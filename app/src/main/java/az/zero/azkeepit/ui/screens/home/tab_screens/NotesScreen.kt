package az.zero.azkeepit.ui.screens.home.tab_screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.data.local.entities.NoteWithFolder
import az.zero.azkeepit.ui.screens.destinations.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.home.HomeViewModel
import az.zero.azkeepit.ui.screens.items.NoteItem
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator,
    notesWithFolder: List<NoteWithFolder>,
    isEditModeOn :Boolean,
    onEditModeChange: () -> Unit,
) {

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(notesWithFolder) { noteWithFolder ->
            NoteItem(
                noteWithFolder = noteWithFolder,
                onLongClick = onEditModeChange,
                onNoteClick = {
                    navigator.navigate(AddEditNoteScreenDestination(noteId = noteWithFolder.note.noteId))
                }
            )
        }
    }
}
