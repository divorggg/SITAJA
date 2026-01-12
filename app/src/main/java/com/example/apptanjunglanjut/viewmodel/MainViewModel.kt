package com.example.apptanjunglanjut

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.apptanjunglanjut.data.AppDatabase
import com.example.apptanjunglanjut.data.entity.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class TransactionType(val displayName: String) {
    IN("Kas Masuk"),
    OUT("Kas Keluar"),
    OPER("Operasional")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "apptanjunglanjut_db"
    ).fallbackToDestructiveMigration().build()

    private val transactionDao = db.transactionDao()
    private val assetDao = db.assetDao()
    private val incentiveDao = db.incentiveDao()
    private val budgetDao = db.budgetDao()

    private val ID_LOCALE = Locale("in", "ID")
    private val ID_FORMATTER = DecimalFormat("#,###", DecimalFormatSymbols(ID_LOCALE))

    // =====================================================
    //  TRANSAKSI
    // =====================================================

    val transactions = transactionDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByType(type.name)
    }

    fun insertTransaction(title: String, amount: Long, description: String, type: TransactionType) {
        viewModelScope.launch {
            transactionDao.insert(
                TransactionEntity(
                    title = title,
                    amount = amount,
                    description = description,
                    type = type.name,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun updateTransaction(id: Int, title: String, amount: Long, description: String, type: TransactionType) {
        viewModelScope.launch {
            transactionDao.update(
                TransactionEntity(
                    id = id,
                    title = title,
                    amount = amount,
                    description = description,
                    type = type.name,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteTransactionById(id: Int) {
        viewModelScope.launch {
            transactionDao.deleteById(id)
        }
    }

    fun deleteAllTransactions(type: TransactionType) {
        viewModelScope.launch {
            transactionDao.deleteTransactionsByType(type)
        }
    }


    // =====================================================
    //  ASET DESA
    // =====================================================

    val assets = assetDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertAsset(name: String, value: Long, note: String) {
        viewModelScope.launch {
            assetDao.insert(AssetEntity(name = name, value = value, note = note))
        }
    }

    fun updateAsset(id: Int, name: String, value: Long, note: String) {
        viewModelScope.launch {
            assetDao.update(
                AssetEntity(
                    id = id,
                    name = name,
                    value = value,
                    note = note
                )
            )
        }
    }

    fun deleteAsset(id: Int) {
        viewModelScope.launch {
            assetDao.deleteAssetById(id)
        }
    }

    fun deleteAllAssets() {
        viewModelScope.launch {
            assetDao.deleteAll()
        }
    }


    // =====================================================
    //  INSENTIF
    // =====================================================

    val incentives = incentiveDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertIncentive(name: String, amount: Long, note: String) {
        viewModelScope.launch {
            incentiveDao.insert(IncentiveEntity(name = name, amount = amount, note = note))
        }
    }

    fun updateIncentive(id: Int, name: String, amount: Long, note: String) {
        viewModelScope.launch {
            incentiveDao.update(
                IncentiveEntity(
                    id = id,
                    name = name,
                    amount = amount,
                    note = note
                )
            )
        }
    }

    fun deleteIncentive(id: Int) {
        viewModelScope.launch {
            incentiveDao.deleteIncentiveById(id)
        }
    }

    fun deleteAllIncentives() {
        viewModelScope.launch {
            incentiveDao.deleteAll()
        }
    }


    // =====================================================
    //  ANGGARAN
    // =====================================================

    val budgets = budgetDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertBudget(name: String, amount: Long, note: String) {
        viewModelScope.launch {
            budgetDao.insert(BudgetEntity(name = name, amount = amount, note = note))
        }
    }

    fun updateBudget(id: Int, name: String, amount: Long, note: String) {
        viewModelScope.launch {
            budgetDao.update(
                BudgetEntity(
                    id = id,
                    name = name,
                    amount = amount,
                    note = note
                )
            )
        }
    }

    fun deleteBudget(id: Int) {
        viewModelScope.launch {
            budgetDao.deleteBudgetById(id)
        }
    }

    fun deleteAllBudgets() {
        viewModelScope.launch {
            budgetDao.deleteAll()
        }
    }
}
