package az.zero.azkeepit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import az.zero.azkeepit.ui.screens.AddEditNoteScreenDestination
import az.zero.azkeepit.ui.screens.FolderDetailsScreenDestination
import az.zero.azkeepit.ui.screens.HomeScreenDestination
import az.zero.azkeepit.ui.screens.SearchScreenDestination
import az.zero.azkeepit.ui.screens.UnlockNoteDialogDestination
import az.zero.azkeepit.ui.screens.folder.details.folderDetailsScreen
import az.zero.azkeepit.ui.screens.home.homeScreen
import az.zero.azkeepit.ui.screens.note.add_edit.addEditNoteScreen
import az.zero.azkeepit.ui.screens.search.searchScreen
import az.zero.azkeepit.ui.screens.unlock_note_dialog.unlockNoteDialog
import az.zero.azkeepit.ui.theme.AZKeepItTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AZKeepItTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = HomeScreenDestination,
                ) {
                    homeScreen(
                        onFolderClick = { folderDetailsScreenArgs ->
                            navController.navigate(
                                FolderDetailsScreenDestination(folderDetailsScreenArgs)
                            )
                        },
                        onSearchClick = {
                            navController.navigate(SearchScreenDestination)
                        },
                        onNoteClick = { noteId ->
                            navController.navigate(AddEditNoteScreenDestination(noteId))
                        },
                        onNoteWithPasswordClick = { noteId ->
                            navController.navigate(UnlockNoteDialogDestination(noteId))
                        },

                    )

                    searchScreen(
                        onBackPressed = navController::navigateUp,
                        onNoteClick = { noteId ->
                            navController.navigate(AddEditNoteScreenDestination(noteId))
                        },
                        onNoteWithPasswordClick = { noteId ->
                            navController.navigate(UnlockNoteDialogDestination(noteId))
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

                    unlockNoteDialog(
                        onPasswordCorrect = { noteId ->
                            navController.popBackStack()
                            navController.navigate(AddEditNoteScreenDestination(noteId))
                        },
                        onDismiss = navController::navigateUp
                    )

                }
            }
        }
    }
}
