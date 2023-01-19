package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    text: String,
    enabled: Boolean = false,
    tint: Color = if (enabled) MaterialTheme.colors.onBackground else Color.Gray,
    textStyle: TextStyle = MaterialTheme.typography.h3.copy(
        color = if (enabled) MaterialTheme.colors.onBackground else Color.Gray
    ),
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickableSafeClick(
                enabled = enabled,
                onClick = onClick
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint)

        Text(text = text, style = textStyle)
    }
}