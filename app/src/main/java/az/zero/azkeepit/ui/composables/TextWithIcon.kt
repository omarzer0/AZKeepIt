package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector? = null,
    iconContentDescription: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableSafeClick(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        if (imageVector != null) {
            Icon(imageVector = imageVector, contentDescription = iconContentDescription)

            Spacer(modifier = Modifier.width(16.dp))
        }
        HeaderText(
            text = text
        )

    }
}

@Composable
fun HeaderText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.body2,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}