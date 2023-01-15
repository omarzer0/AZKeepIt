package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.zero.azkeepit.ui.theme.bgColor
import az.zero.azkeepit.ui.theme.cardBgColor


data class BottomSheetDateItem(
    val title: String,
    val onClick: () -> Unit,
    val dismissAfterClick: Boolean = true,
)

@Composable
fun BottomSheetWithItems(
    modifier: Modifier = Modifier,
    items: List<BottomSheetDateItem>,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {

    Column(
        modifier = modifier
            .background(bgColor)
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(cardBgColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (header == null) Spacer(modifier = Modifier.height(16.dp))
        else header()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) {
                BottomSheetItem(
                    text = it.title,
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

@Composable
fun BottomSheetItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableSafeClick(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.body2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
