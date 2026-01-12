package com.example.apptanjunglanjut.data.repository

import com.example.apptanjunglanjut.data.dao.BudgetDao
import com.example.apptanjunglanjut.data.entity.BudgetEntity

class BudgetRepository(private val dao: BudgetDao) {
    fun getAllBudgets() = dao.getAll()
    suspend fun insert(budget: BudgetEntity) = dao.insert(budget)

    suspend fun deleteAll() = dao.deleteAll()
}
