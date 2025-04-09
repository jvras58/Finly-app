package com.equipealpha.financaspessoais.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.equipealpha.financaspessoais.data.entities.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Transaction?

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactionsFlow(): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'entrada'")
    fun getTotalEntradasFlow(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'saida'")
    fun getTotalSaidasFlow(): Flow<Double?>
}
