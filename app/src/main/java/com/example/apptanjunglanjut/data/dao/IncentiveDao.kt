package com.example.apptanjunglanjut.data.dao

import androidx.room.*
import com.example.apptanjunglanjut.data.entity.IncentiveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncentiveDao {
    @Query("SELECT * FROM incentives")
    fun getAll(): Flow<List<IncentiveEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incentive: IncentiveEntity)

    @Delete
    suspend fun delete(incentive: IncentiveEntity)

    @Query("DELETE FROM incentives")
    suspend fun deleteAll()

    @Query("DELETE FROM incentives WHERE id = :id")
    suspend fun deleteIncentiveById(id: Int)

    @Update
    suspend fun update(incentive: IncentiveEntity)
}
