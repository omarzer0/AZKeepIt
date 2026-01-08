package az.zero.azkeepit.ui.screens

import az.zero.azkeepit.ui.screens.folder.details.FolderDetailsScreenArgs
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenDestination

@Serializable
object SearchScreenDestination

//@Serializable
//data class FolderDetailsScreenDestination(val folderDetailsScreenArgs: FolderDetailsScreenArgs)

// TODO remove this
//@Serializable
//data class FolderDetailsScreenDestination(
//    val id: Long,
//    val folderName: String
//)

@Serializable
data class FolderDetailsScreenDestination(
    val folderDetailsScreenArgs: FolderDetailsScreenArgs
) {
    companion object {
        val typeMap = FolderDetailsScreenArgs.typeMap
    }
}


@Serializable
data class AddEditNoteScreenDestination(val noteId: Long?)


