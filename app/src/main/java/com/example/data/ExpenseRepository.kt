package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("expense_prefs", Context.MODE_PRIVATE)

    private val _salary = MutableStateFlow(prefs.getLong("monthly_salary", 0L))
    val salary = _salary.asStateFlow()

    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun setSalary(amount: Long) {
        prefs.edit().putLong("monthly_salary", amount).apply()
        _salary.value = amount
    }

    suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(id: String) {
        expenseDao.deleteExpenseById(id)
    }

    suspend fun clearAllExpenses() {
        expenseDao.clearAllExpenses()
    }
}
