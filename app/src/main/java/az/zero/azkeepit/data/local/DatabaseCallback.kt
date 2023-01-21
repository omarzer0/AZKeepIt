package az.zero.azkeepit.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(
    private val database: Provider<AppDatabase>,
) : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
    }

}