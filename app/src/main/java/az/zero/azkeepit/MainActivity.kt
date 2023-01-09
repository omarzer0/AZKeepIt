package az.zero.azkeepit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import az.zero.azkeepit.ui.composables.ChangeStatusBarColor
import az.zero.azkeepit.ui.screens.NavGraphs
import az.zero.azkeepit.ui.theme.AZKeepItTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AZKeepItTheme {
                ChangeStatusBarColor(
                    statusColor = MaterialTheme.colors.background,
                    navigationBarColor = MaterialTheme.colors.background,
                )
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
