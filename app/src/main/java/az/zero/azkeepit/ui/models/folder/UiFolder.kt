package az.zero.azkeepit.ui.models.folder

data class UiFolder(
    val folderId: Long,
    val name: String,
    val createdAt: Long,
    val isSelected: Boolean = false,
)