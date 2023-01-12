package az.zero.azkeepit.ui.screens.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.data.local.entities.Note
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.theme.cardBgColor
import az.zero.azkeepit.util.JDateTimeUtil

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClick: () -> Unit,
) {

    val shortDateTime = remember(note.createdAt) {
        JDateTimeUtil.toShortDateTimeFormat(note.createdAt)
    }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickableSafeClick(onClick = onNoteClick),
        backgroundColor = cardBgColor,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = note.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h2.copy(
                    color = MaterialTheme.colors.onBackground,
                )
            )

            Spacer(modifier = Modifier.size(30.dp))

            Text(
                text = note.content,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onBackground,
                )
            )

            Spacer(modifier = Modifier.size(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier= Modifier.weight(2f),
                    text = shortDateTime,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h3.copy(
                        color = MaterialTheme.colors.onBackground,
                    )
                )

                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    modifier= Modifier.weight(2f),
                    text = note.folderName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.h3.copy(
                        color = MaterialTheme.colors.onBackground,
                    )
                )
            }
        }
    }
}