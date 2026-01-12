package com.example.apptanjunglanjut.data.dao

import androidx.room.*
import com.example.apptanjunglanjut.TransactionType
import com.example.apptanjunglanjut.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    fun getTransactionsByType(type: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("DELETE FROM transactions WHERE type = :type")
    suspend fun deleteTransactionsByType(type: TransactionType)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Update
    suspend fun update(transaction: TransactionEntity)

}

