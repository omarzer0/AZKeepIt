package az.zero.azkeepit.domain.models.note

import az.zero.azkeepit.domain.models.folder.DomainFolder

data class DomainNote(
    val noteId: Long,
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val images: List<String>,
    val colorArgb: Int,
    val hashedPassword: String?,
    val ownerDomainFolder: DomainFolder?,
)