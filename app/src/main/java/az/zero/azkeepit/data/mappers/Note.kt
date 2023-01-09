package az.zero.azkeepit.data.mappers

import az.zero.azkeepit.data.local.entities.LocalNote
import az.zero.azkeepit.domain.model.UiNote
import az.zero.azkeepit.util.JDateTimeUtil.toLongDateTimeFormat
import az.zero.azkeepit.util.JDateTimeUtil.toShortDateTimeFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun UiNote.toLocalNote(): LocalNote = LocalNote(
    title = title,
    content = content,
    isLocked = isLocked,
    isUpdated = isUpdated,
    createdAt = createdAt,
    updatedAt = updatedAt,
    folderName = folderName,
    ownerFolderId = ownerFolderId,
    noteId = noteId
)

fun LocalNote.toUiNote(): UiNote {
    val createdShortDateTime = toShortDateTimeFormat(createdAt)
    val createdLongDateTime = toLongDateTimeFormat(createdAt)

    val updatedShortDateTime = updatedAt?.let { toShortDateTimeFormat(it) }
    val updatedLongDateTime = updatedAt?.let { toLongDateTimeFormat(it) }

    return UiNote(
        title = title,
        content = content,
        isLocked = isLocked,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isUpdated = isUpdated,
        createdShortDateTime = createdShortDateTime,
        createdLongDateTime = createdLongDateTime,
        updatedShortDateTime = updatedShortDateTime,
        updatedLongDateTime = updatedLongDateTime,
        folderName = folderName,
        ownerFolderId = ownerFolderId!!,
        noteId = noteId!!
    )
}


fun Flow<List<LocalNote>>.toFlowListOfUiNote(): Flow<List<UiNote>> {
    return this.map { list ->
        list.filter { it.noteId != null && it.ownerFolderId != null }
            .map { it.toUiNote() }
    }
}