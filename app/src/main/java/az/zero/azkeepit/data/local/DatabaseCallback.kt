package az.zero.azkeepit.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import az.zero.azkeepit.data.local.entities.LocalFolder
import az.zero.azkeepit.data.local.entities.LocalNote
import az.zero.azkeepit.util.JDateTimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(
    private val database: Provider<AppDatabase>,
) : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        val dao = database.get().getNoteDao()
        val scope = CoroutineScope(SupervisorJob())
        scope.launch {

            notes.forEach { launch { dao.insertNote(it) } }

            folders.forEach { launch { dao.insertFolder(it) } }
        }
    }

}

val notes = List(20) {
    val folderName = if (it < 10) "folder 1" else "folder 2"
    val ownerFolderId = if (it < 10) 0 else 1
    LocalNote(title = "title $it",
        content = "content $it",
        isLocked = false,
        isUpdated = false,
        createdAt = JDateTimeUtil.now(),
        updatedAt = null,
        folderName = folderName,
        ownerFolderId = ownerFolderId.toLong(),
        noteId = it.toLong()
    )
}

val folders = List(2) {
    LocalFolder("folder $it", it.toLong())
}