package az.zero.azkeepit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = fabColor,
    onSecondary = Color.White,
    onPrimary = Color.White,
    background = bgColor,
    surface = bgColor
)

private val LightColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = fabColor,
    onSecondary = Color.White,
    onPrimary = Color.White,
    background = bgColor,
    surface = bgColor
)

//private val LightColorPalette = lightColors(
//    primary = Purple500,
//    primaryVariant = Purple700,
//    secondary = Teal200,
//    onPrimary = Color.Black,
//    background = bgColor
//)

/*
* fab -> #706fc8
* card -> #262636
* bg -> #1f1d2c
* */

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