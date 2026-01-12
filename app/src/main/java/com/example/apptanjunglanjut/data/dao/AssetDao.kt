package com.example.apptanjunglanjut.data.dao

import androidx.room.*
import com.example.apptanjunglanjut.data.entity.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM assets ORDER BY id DESC")
    fun getAll(): Flow<List<AssetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asset: AssetEntity)

    @Query("DELETE FROM assets")
    suspend fun deleteAll()

    @Query("DELETE FROM assets WHERE id = :id")
    suspend fun deleteAssetById(id: Int)

    @Update
    suspend fun update(asset: AssetEntity)
}
