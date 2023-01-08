package az.zero.azkeepit.domain.model

data class UiNote(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val date: Long,
    val time: Long,
    val folderName: String,
    val ownerFolderId: Long,
    val noteId: Long,
)