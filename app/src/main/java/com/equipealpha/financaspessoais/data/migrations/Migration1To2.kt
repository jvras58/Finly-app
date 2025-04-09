package com.equipealpha.financaspessoais.data.migrations
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user_profile id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "        nome TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "senha TEXT NOT NULL," +
                "avatarUri TEXT NOT NULL)" +
                "")
    }
}