package com.example.apptanjunglanjut.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apptanjunglanjut.data.dao.*
import com.example.apptanjunglanjut.data.entity.*

@Database(
    entities = [
        TransactionEntity::class,
        AssetEntity::class,
        IncentiveEntity::class,
        BudgetEntity::class // ✅ tambahkan ini!
    ],
    version = 1, // ⬆️ naikkan versi agar Room rebuild DB
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun assetDao(): AssetDao
    abstract fun incentiveDao(): IncentiveDao
    abstract fun budgetDao(): BudgetDao
}
