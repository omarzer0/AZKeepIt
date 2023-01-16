package az.zero.azkeepit.data.local.entities

data class UiFolderWithUiNotes(
    val folder: Folder?,
    val noteWithFolders: List<NoteWithFolder>,
)