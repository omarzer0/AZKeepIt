package az.zero.azkeepit.ui.screens.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.twotone.Lock
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
import az.zero.azkeepit.domain.mappers.UiNote
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.theme.cardBgColor
import az.zero.azkeepit.ui.theme.selectedColor

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    uiNote: UiNote,
    isEditModeOn: Boolean,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onNoteClick: () -> Unit,
) {

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickableSafeClick(
                onClick = onNoteClick,
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
            ),
        backgroundColor = cardBgColor,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            if (isEditModeOn) {
                RadioButton(
                    modifier = Modifier.align(Alignment.Start),
                    selected = uiNote.isSelected,
                    enabled = uiNote.isSelected,
                    colors = RadioButtonDefaults.colors(selectedColor = selectedColor),
                    onClick = null
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = uiNote.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h2.copy(
                    color = MaterialTheme.colors.onBackground,
                )
            )

            Spacer(modifier = Modifier.size(30.dp))

            if (!uiNote.isLocked) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
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
            }else{
                Text(
                    text = uiNote.content,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onBackground,
                    )
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
                    style = MaterialTheme.typography.h3.copy(
                        color = MaterialTheme.colors.onBackground,
                    )
                )

                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    modifier = Modifier.weight(2f),
                    text = uiNote.ownerUiFolder?.name ?: "",
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