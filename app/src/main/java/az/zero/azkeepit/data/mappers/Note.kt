package az.zero.azkeepit.data.mappers

import az.zero.azkeepit.data.local.entities.LocalNote
import az.zero.azkeepit.domain.model.UiNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun UiNote.toLocalNote(): LocalNote = LocalNote(
    title, content, isLocked, date, time, folderName, ownerFolderId, noteId
)

fun LocalNote.toUiNote(): UiNote = UiNote(
    title, content, isLocked, date, time, folderName, ownerFolderId!!, noteId!!
)

fun Flow<List<LocalNote>>.toFlowListOfUiNote(): Flow<List<UiNote>> {
    return this.map { list ->
        list.filter { it.noteId != null && it.ownerFolderId != null }
            .map { it.toUiNote() }
    }
}