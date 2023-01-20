package az.zero.azkeepit.ui.screens.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.domain.mappers.UiFolder
import az.zero.azkeepit.ui.composables.clickableSafeClick
import az.zero.azkeepit.ui.theme.cardBgColor
import az.zero.azkeepit.ui.theme.selectedColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridItemScope.FolderItem(
    modifier: Modifier = Modifier,
    uiFolder: UiFolder,
    isEditModeOn: Boolean,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onFolderClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickableSafeClick(
                onClick = onFolderClick,
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
            )
            .animateItemPlacement(),
        backgroundColor = cardBgColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isEditModeOn) {
                RadioButton(
                    modifier = Modifier.align(Alignment.Start),
                    selected = uiFolder.isSelected,
                    enabled = uiFolder.isSelected,
                    colors = RadioButtonDefaults.colors(selectedColor = selectedColor),
                    onClick = null
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.folder),
                contentDescription = stringResource(R.string.folder)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiFolder.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h2.copy(
                    color = MaterialTheme.colors.onBackground,
                )
            )
        }
    }
}