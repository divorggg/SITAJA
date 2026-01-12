package com.example.apptanjunglanjut.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Long,
    val description: String,
    val type: String, // "IN", "OUT", "OPER"
    val timestamp: Long = System.currentTimeMillis() // waktu transaksi
)
