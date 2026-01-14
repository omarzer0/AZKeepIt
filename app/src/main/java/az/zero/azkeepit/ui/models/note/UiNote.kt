package az.zero.azkeepit.ui.models.note

import android.net.Uri
import androidx.compose.ui.graphics.Color
import az.zero.azkeepit.ui.models.folder.UiFolder

data class UiNote(
    val noteId: Long,
    val title: String,
    val content: String,
    val isLocked: Boolean,
    val createdAt: Long,
    val shortDateTime: String,
    val longDateTime: String,
    val images: List<Uri>,
    val color: Color,
    val hashedPassword: String?,
    val isSelected: Boolean = false,
    val ownerUiFolder: UiFolder? = null,
)