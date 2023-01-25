package az.zero.azkeepit.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ChangeSystemBarColor(
    systemUiController: SystemUiController = rememberSystemUiController(),
    statusColor: Color? = null,
    navigationBarColor: Color? = null,
) {

    if (statusColor != null) {
        ChangeStatusBarColor(
            systemUiController = systemUiController,
            statusColor = statusColor
        )
    }

    if (navigationBarColor != null) {
        ChangeNavigationBarColor(
            systemUiController = systemUiController,
            navigationBarColor = navigationBarColor
        )
    }

}

data class EnterExitColors(
    val enterColor: Color,
    val exitStatusColor: Color,
)

@Composable
fun ChangeSystemBarsColorAndRevertWhenClose(
    key: Any?,
    systemUiController: SystemUiController = rememberSystemUiController(),
    statusBarColors: EnterExitColors? = null,
    navigationBarColors: EnterExitColors? = null,
) {

    DisposableEffect(key1 = key) {
        if (statusBarColors != null) systemUiController.setStatusBarColor(color = statusBarColors.enterColor)
        if (navigationBarColors != null) systemUiController.setNavigationBarColor(color = navigationBarColors.enterColor)

        onDispose {
            if (statusBarColors != null) systemUiController.setStatusBarColor(color = statusBarColors.exitStatusColor)
            if (navigationBarColors != null) systemUiController.setNavigationBarColor(color = navigationBarColors.exitStatusColor)
        }
    }

}

@Composable
fun ChangeStatusBarColor(
    systemUiController: SystemUiController = rememberSystemUiController(),
    statusColor: Color,
    useDarkIcons: Boolean = !statusColor.isColorDark(),
) {
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusColor,
            darkIcons = useDarkIcons
        )
    }
}

@Composable
fun ChangeNavigationBarColor(
    systemUiController: SystemUiController = rememberSystemUiController(),
    navigationBarColor: Color,
    useDarkIcons: Boolean = !navigationBarColor.isColorDark(),
) {

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = useDarkIcons
        )
    }
}

fun changeSystemBarColor(
    systemUiController: SystemUiController,
    navigationBarColor: Color,
    useDarkIcons: Boolean = !navigationBarColor.isColorDark(),
) {
    systemUiController.setNavigationBarColor(
        color = navigationBarColor,
        darkIcons = useDarkIcons
    )
}