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

@Composable
fun ChangeSystemBarColorAndRevertWhenClose(
    key: Any?,
    systemUiController: SystemUiController = rememberSystemUiController(),
    initialStatusColor: Color,
    newStatusColor: Color,
    initialNavigationBarColor: Color,
    newNavigationBarColor: Color,
) {

    DisposableEffect(key1 = key) {
        systemUiController.setStatusBarColor(color = newStatusColor)
        systemUiController.setNavigationBarColor(color = newNavigationBarColor)

        onDispose {
            systemUiController.setStatusBarColor(color = initialStatusColor)
            systemUiController.setNavigationBarColor(color = initialNavigationBarColor)
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