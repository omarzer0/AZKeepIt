package az.zero.azkeepit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import az.zero.azkeepit.ui.screens.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.FolderDetailsScreenDestination
import az.zero.azkeepit.ui.screens.HomeScreenDestination
import az.zero.azkeepit.ui.screens.SearchScreenDestination
import az.zero.azkeepit.ui.screens.folder.details.folderDetailsScreen
import az.zero.azkeepit.ui.screens.home.homeScreen
import az.zero.azkeepit.ui.screens.note.add_edit.addEditNoteScreen
import az.zero.azkeepit.ui.screens.search.searchScreen
import az.zero.azkeepit.ui.theme.AZKeepItTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AZKeepItTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = HomeScreenDestination,
                ) {
                    homeScreen(
                        onSearchClick = { navController.navigate(SearchScreenDestination) },
                        onNavigateToAddEditNoteScreen = { noteId ->
                            navController.navigate(AddEditNoteScreenDestination(noteId))
                        },
                        onNavigateToFolderDetailsScreen = { folderDetailsScreenArgs ->
                            navController.navigate(
                                FolderDetailsScreenDestination(folderDetailsScreenArgs)
                            )
                        },
                    )

                    searchScreen(
                        onBackPressed = navController::navigateUp,
                        onNoteClick = { noteId ->
                            navController.navigate(AddEditNoteScreenDestination(noteId))
                        },
                    )

                    addEditNoteScreen(
                        onBackPressed = navController::navigateUp
                    )

                    folderDetailsScreen(
                        onBackPressed = navController::navigateUp,
                        onNoteClick = { noteId ->
                            navController.navigate(AddEditNoteScreenDestination(noteId))
                        }
                    )
                }
            }
        }
    }
}
