package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.data.local.entities.Note

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    textStyle: TextStyle = TextStyle(
        color = MaterialTheme.colors.onBackground
    ),
    onNoteClick: () -> Unit,
) {

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickableSafeClick(onClick = onNoteClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (note.noteId?.toInt()?.mod( 2 ) == 0) {
                    Spacer(modifier = Modifier.size(30.dp))
                    Text(text = note.title, style = textStyle)
                    Spacer(modifier = Modifier.size(30.dp))
                } else {
                    Spacer(modifier = Modifier.size(30.dp))
                    Text(text = note.title, style = textStyle)
                    Text(text = note.content, style = textStyle)
                    Text(text = note.folderName, style = textStyle)
                    Spacer(modifier = Modifier.size(30.dp))
                }

            }
        }

    }
}