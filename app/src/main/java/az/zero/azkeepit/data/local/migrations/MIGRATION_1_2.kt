import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import az.zero.azkeepit.domain.hashing.PasswordHasher

fun createMigration1To2(passwordHasher: PasswordHasher): Migration {
    return object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {

            database.execSQL("""
                CREATE TABLE IF NOT EXISTS Note_new (
                    noteId INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    content TEXT NOT NULL,
                    isLocked INTEGER NOT NULL,
                    createdAt INTEGER NOT NULL,
                    ownerFolderId INTEGER,
                    images TEXT NOT NULL,
                    color INTEGER NOT NULL,
                    hashedPassword TEXT
                )
            """.trimIndent())

            val cursor = database.query("SELECT noteId, title, content, isLocked, createdAt, ownerFolderId, images, color, password FROM Note")

            while (cursor.moveToNext()) {
                val noteId = cursor.getLong(0)
                val title = cursor.getString(1)
                val content = cursor.getString(2)
                val isLocked = cursor.getInt(3)
                val createdAt = cursor.getLong(4)
                val ownerFolderId = if (cursor.isNull(5)) null else cursor.getLong(5)
                val images = cursor.getString(6)
                val color = cursor.getInt(7)
                val plainPassword = if (cursor.isNull(8)) null else cursor.getString(8)

                val hashedPassword = plainPassword?.let { passwordHasher.hash(it) }

                database.execSQL("""
                    INSERT INTO Note_new (noteId, title, content, isLocked, createdAt, ownerFolderId, images, color, hashedPassword)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                    arrayOf(noteId, title, content, isLocked, createdAt, ownerFolderId, images, color, hashedPassword)
                )
            }
            cursor.close()

            database.execSQL("DROP TABLE Note")

            database.execSQL("ALTER TABLE Note_new RENAME TO Note")
        }
    }
}