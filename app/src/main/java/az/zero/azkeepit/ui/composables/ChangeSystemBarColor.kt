package az.zero.azkeepit.ui.composables

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ChangeSystemBarColor(
    statusColor: Color? = null,
    navigationBarColor: Color? = null,
) {
    if (statusColor != null) {
        ChangeStatusBarColor(statusColor = statusColor)
    }

    if (navigationBarColor != null) {
        ChangeNavigationBarColor(navigationBarColor = navigationBarColor)
    }
}

data class EnterExitColors(
    val enterColor: Color,
    val exitStatusColor: Color,
)

@Composable
fun ChangeSystemBarsColorAndRevertWhenClose(
    key: Any?,
    statusBarColors: EnterExitColors? = null,
    navigationBarColors: EnterExitColors? = null,
) {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window

    DisposableEffect(key1 = key) {
        window?.let {
            if (statusBarColors != null) {
                it.statusBarColor = statusBarColors.enterColor.toArgb()
            }
            if (navigationBarColors != null) {
                it.navigationBarColor = navigationBarColors.enterColor.toArgb()
            }
        }

        onDispose {
            window?.let {
                if (statusBarColors != null) {
                    it.statusBarColor = statusBarColors.exitStatusColor.toArgb()
                }
                if (navigationBarColors != null) {
                    it.navigationBarColor = navigationBarColors.exitStatusColor.toArgb()
                }
            }
        }
    }
}

@Composable
fun ChangeStatusBarColor(
    statusColor: Color,
    useDarkIcons: Boolean = !statusColor.isColorDark(),
) {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window

    SideEffect {
        window?.let {
            it.statusBarColor = statusColor.toArgb()
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = useDarkIcons
        }
    }
}

@Composable
fun ChangeNavigationBarColor(
    navigationBarColor: Color,
    useDarkIcons: Boolean = !navigationBarColor.isColorDark(),
) {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window

    SideEffect {
        window?.let {
            it.navigationBarColor = navigationBarColor.toArgb()
            WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = useDarkIcons
        }
    }
}

fun changeSystemBarColor(
    activity: Activity,
    navigationBarColor: Color,
    useDarkIcons: Boolean = !navigationBarColor.isColorDark(),
) {
    activity.window?.let { window ->
        window.navigationBarColor = navigationBarColor.toArgb()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightNavigationBars = useDarkIcons
    }
}