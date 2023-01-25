package az.zero.azkeepit.ui.composables

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import az.zero.azkeepit.ui.screens.note.add_edit.getCorrectLightOrDarkColor


@Composable
@Stable
fun headerTextStyle(surfaceColor: Color? = null): TextStyle = getLuminanceTextStyle(
    surfaceColor = surfaceColor,
    textStyle = MaterialTheme.typography.h1
)


@Composable
@Stable
fun smallHeaderTextStyle(surfaceColor: Color? = null): TextStyle = getLuminanceTextStyle(
    surfaceColor = surfaceColor,
    textStyle = MaterialTheme.typography.h3
)

@Composable
@Stable
fun bodyTextStyle(surfaceColor: Color? = null): TextStyle = getLuminanceTextStyle(
    surfaceColor = surfaceColor,
    textStyle = MaterialTheme.typography.body1
)

@Composable
@Stable
fun smallTextStyle(surfaceColor: Color? = null): TextStyle = getLuminanceTextStyle(
    surfaceColor = surfaceColor,
    textStyle = MaterialTheme.typography.body2
)

@Composable
@Stable
fun getLuminanceTextStyle(
    surfaceColor: Color? = null,
    textStyle: TextStyle,
): TextStyle {
    return textStyle.copy(
        color = surfaceColor?.getCorrectLightOrDarkColor() ?: MaterialTheme.colors.onBackground
    )
}
