//package az.zero.azkeepit.data.mappers
//
//import az.zero.azkeepit.data.local.entities.Note
//import az.zero.azkeepit.domain.model.UiNote
//import az.zero.azkeepit.util.JDateTimeUtil.toLongDateTimeFormat
//import az.zero.azkeepit.util.JDateTimeUtil.toShortDateTimeFormat
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//fun UiNote.toLocalNote(): Note = Note(
//    title = title,
//    content = content,
//    isLocked = isLocked,
//    createdAt = createdAt,
//    folderName = folderName,
//    ownerFolderId = ownerFolderId,
//    noteId = noteId
//)
//
//fun Note.toUiNote(): UiNote {
//    val createdShortDateTime = toShortDateTimeFormat(createdAt)
//    val createdLongDateTime = toLongDateTimeFormat(createdAt)
//
//    return UiNote(
//        title = title,
//        content = content,
//        isLocked = isLocked,
//        createdAt = createdAt,
//        createdShortDateTime = createdShortDateTime,
//        createdLongDateTime = createdLongDateTime,
//        folderName = folderName,
//        ownerFolderId = ownerFolderId,
//        noteId = noteId
//    )
//}
//
//
//fun Flow<List<Note>>.toFlowListOfUiNote(): Flow<List<UiNote>> {
//    return this.map { list ->
//        list.map {
//            it.toUiNote()
//        }
//    }
//}