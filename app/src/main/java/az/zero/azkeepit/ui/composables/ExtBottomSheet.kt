package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


data class BottomSheetDateItem(
    val title: String,
    val imageVector: ImageVector? = null,
    val iconContentDescription: String? = null,
    val dismissAfterClick: Boolean = true,
    val onClick: () -> Unit,
)

@Composable
fun BottomSheetWithItems(
    modifier: Modifier = Modifier,
    items: List<BottomSheetDateItem>,
    backgroundColor: Color = MaterialTheme.colors.background,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {

    Column(
        modifier = modifier
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (header == null) Spacer(modifier = Modifier.height(16.dp))
        else header()

        LazyColumn {
            items(items) {
                TextWithIcon(
                    text = it.title,
                    imageVector = it.imageVector,
                    iconContentDescription = it.iconContentDescription,
                    onClick = {
                        it.onClick()
                        if (it.dismissAfterClick) onDismiss?.invoke()
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

}


