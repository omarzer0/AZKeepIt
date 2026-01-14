package az.zero.azkeepit.domain.models.note

data class DomainNote(
    val noteId: Long,
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val ownerFolderId: Long?,
    val images: List<String>,
    val colorArgb: Int,
    val hashedPassword: String?,
)