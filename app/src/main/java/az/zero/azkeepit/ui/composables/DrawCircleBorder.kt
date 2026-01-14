package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DrawCircleBorder(
    borderWidth: Dp = 0.5.dp,
    content: @Composable () -> Unit,
) {
    return Box(modifier = Modifier
        .border(
            width = borderWidth,
            color = Color.Gray,
            shape = CircleShape
        )
        .padding(2.dp)) {
        content()
    }
}