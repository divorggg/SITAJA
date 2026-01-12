package com.example.apptanjunglanjut.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incentives")
data class IncentiveEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Long,
    val note: String
)
