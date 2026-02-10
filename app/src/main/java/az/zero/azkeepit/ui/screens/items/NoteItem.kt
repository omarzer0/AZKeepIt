package az.zero.azkeepit.ui.screens.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.ui.composables.SlidingImage
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.models.note.UiNote
import az.zero.azkeepit.ui.screens.note.add_edit.getCorrectLightOrDarkColor
import az.zero.azkeepit.ui.theme.selectedColor

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    uiNote: UiNote,
    isEditModeEnabled: Boolean,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onNoteClick: (noteId: Long) -> Unit,
) {

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickableSafeClick(
                onClick = { onNoteClick(uiNote.noteId) },
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
            ),
        backgroundColor = uiNote.color,
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {

                if (uiNote.images.isNotEmpty() && uiNote.isLocked.not()) {
                    SlidingImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        dataUris = uiNote.images
                    )
                }

                if (isEditModeEnabled) {
                    RadioButton(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopStart),
                        selected = uiNote.isSelected,
                        enabled = uiNote.isSelected,
                        colors = RadioButtonDefaults.colors(selectedColor = selectedColor),
                        onClick = null
                    )

                }
            }

            NoteTextSection(uiNote = uiNote)
        }

    }
}

@Composable
private fun NoteTextSection(
    uiNote: UiNote,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        if (uiNote.isLocked) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "lock"
                )
            }
        } else {
            Text(
                text = uiNote.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h2.copy(color = uiNote.color.getCorrectLightOrDarkColor())
            )
            Text(
                text = uiNote.content,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1.copy(color = uiNote.color.getCorrectLightOrDarkColor())
            )
        }


        Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(2f),
                text = uiNote.shortDateTime,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h3.copy(color = uiNote.color.getCorrectLightOrDarkColor())
            )

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                modifier = Modifier.weight(2f),
                text = uiNote.ownerUiFolder?.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.h3.copy(color = uiNote.color.getCorrectLightOrDarkColor())
            )
        }
    }
}