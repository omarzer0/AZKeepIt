package az.zero.azkeepit.domain.model

data class UiFolder(
    val folderId: Long,
    val folderName: String,
)

data class UiFolderWithNotes(
    val folderId: Long,
    val folderName: String,
    val notes: List<UiNote>,
)
