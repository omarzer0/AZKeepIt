package az.zero.azkeepit.ui.models.note

import android.net.Uri
import androidx.compose.ui.graphics.Color
import az.zero.azkeepit.domain.commons.INVALID_ID
import az.zero.azkeepit.ui.composables.ColorPallet.DarkHex
import az.zero.azkeepit.ui.composables.getColorFromHex
import az.zero.azkeepit.ui.models.folder.UiFolder
import az.zero.azkeepit.util.JDateTimeUtil

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
    val password: String?,
    val isSelected: Boolean = false,
    val ownerUiFolder: UiFolder? = null,
)

private val createdDate = JDateTimeUtil.now()

val emptyUiNote = UiNote(
    noteId = INVALID_ID,
    title = "",
    content = "",
    createdAt = createdDate,
    shortDateTime = JDateTimeUtil.toShortDateTimeFormat(createdDate),
    longDateTime = JDateTimeUtil.toLongDateTimeFormat(createdDate),
    images = emptyList(),
    color = getColorFromHex(DarkHex),
    password = null,
    isSelected = false,
    isLocked = false,
    ownerUiFolder = null
)