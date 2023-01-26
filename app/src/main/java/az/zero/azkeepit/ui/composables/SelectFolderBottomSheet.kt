package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.R
import az.zero.azkeepit.domain.mappers.UiFolder
import az.zero.azkeepit.ui.theme.cardBgColor
import az.zero.azkeepit.ui.theme.selectedColor

@Composable
fun SelectFolderBottomSheet(
    modifier: Modifier = Modifier,
    titleText:String,
    uiFolders: List<UiFolder>,
    backgroundColor: Color = MaterialTheme.colors.background,
    onClick: (uiFolder: UiFolder) -> Unit,
    onDismiss: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val bottomSheetHeight = configuration.screenHeightDp.dp / 2
    val items = uiFolders.map { BottomSheetDateItem(title = it.name, onClick = { onClick(it) }) }

    BottomSheetWithItems(
        modifier = modifier
            .fillMaxWidth()
            .height(bottomSheetHeight),
        items = items,
        backgroundColor=backgroundColor,
        onDismiss = onDismiss,
        header = {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = titleText,
                style = MaterialTheme.typography.h2.copy(color = selectedColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )

}