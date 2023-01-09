package az.zero.azkeepit.domain.model

data class UiNote(
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val isUpdated:Boolean,
    val createdAt: Long,
    val updatedAt: Long?,

    val createdShortDateTime: String,
    val createdLongDateTime: String,
    val updatedShortDateTime: String?,
    val updatedLongDateTime: String?,

    val folderName: String,
    val ownerFolderId: Long,
    val noteId: Long,
)