package az.zero.azkeepit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = selectedColor,
    primaryVariant = Purple700,
    secondary = fabColor,
    onSecondary = Color.White,
    onPrimary = Color.White,
    background = bgColor,
    onBackground = Color.White,
    surface = bgColor,
    onSurface = Color.White
)

private val LightColorPalette = darkColors(
    primary = selectedColor,
    primaryVariant = Purple700,
    secondary = fabColor,
    onSecondary = Color.White,
    onPrimary = Color.White,
    background = bgColor,
    onBackground = Color.White,
    surface = bgColor,
    onSurface = Color.White
)

@Composable
fun AZKeepItTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}