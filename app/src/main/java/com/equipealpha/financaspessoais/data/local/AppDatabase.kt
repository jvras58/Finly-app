package com.equipealpha.financaspessoais.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.equipealpha.financaspessoais.data.dao.UserProfileDao
import com.equipealpha.financaspessoais.data.entities.Transaction
import com.equipealpha.financaspessoais.data.entities.UserProfile
import com.equipealpha.financaspessoais.data.local.dao.TransactionDao
import com.equipealpha.financaspessoais.data.migrations.MIGRATION_1_2

@Database(entities = [Transaction::class, UserProfile::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_db"
                )
                    .addMigrations(MIGRATION_1_2) // Registra a migração 1 para 2
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}