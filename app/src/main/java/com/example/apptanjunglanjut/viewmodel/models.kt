package com.example.apptanjunglanjut.viewmodel

import java.time.LocalDate


// Simple models for each feature
data class Anggaran(val id: Int, val title: String, val amount: Long, val note: String?)


data class Modal(val id: Int, val source: String, val amount: Long, val date: String)


data class Transaksi(val id: Int, val description: String, val amount: Long, val isIncome: Boolean, val date: String)


data class Operasional(val id: Int, val category: String, val amount: Long, val date: String)


data class Insentif(val id: Int, val recipient: String, val amount: Long, val note: String?)


data class Aset(val id: Int, val name: String, val value: Long, val condition: String)