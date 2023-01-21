package az.zero.azkeepit.ui.composables

import androidx.compose.runtime.Composable
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