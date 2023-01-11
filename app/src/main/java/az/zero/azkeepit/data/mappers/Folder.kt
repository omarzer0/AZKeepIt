//package az.zero.azkeepit.data.mappers
//
//import az.zero.azkeepit.data.local.entities.Folder
//import az.zero.azkeepit.data.local.entities.LocalFolderWithNotes
//import az.zero.azkeepit.domain.model.UiFolder
//import az.zero.azkeepit.domain.model.UiFolderWithNotes
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//fun UiFolder.toLocalFolder() = Folder(
//    name = folderName,
//    folderId = folderId,
//)
//
//fun LocalFolderWithNotes.toUiFolderWithNotes() = UiFolderWithNotes(
//    folderId = folder.folderId!!,
//    folderName = folder.name,
//    notes = notes.map { it.toUiNote() }
//)
//
//fun Folder.toUiFolder() = UiFolder(
//    folderId = folderId!!,
//    folderName = name
//)
//
//fun Flow<List<Folder>>.toFlowListOfUiFolders(): Flow<List<UiFolder>> {
//    return this.map { list ->
//        list.filter { it.folderId != null }
//            .map { it.toUiFolder() }
//    }
//}
//
//fun Flow<List<LocalFolderWithNotes>>.toFlowListOfUiFolderWithNotes(): Flow<List<UiFolderWithNotes>> {
//    return this.map { list ->
//        list.map { it.toUiFolderWithNotes() }
//    }
//}