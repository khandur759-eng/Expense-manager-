package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Expense
import com.example.data.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExpenseRepository

    val salary: StateFlow<Long>
    val expenses: StateFlow<List<Expense>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ExpenseRepository(db.expenseDao(), application)
        salary = repository.salary
        expenses = repository.allExpenses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun saveSalary(amount: Long) {
        repository.setSalary(amount)
    }

    fun addExpense(amount: Long, category: String, note: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayStr = dateFormat.format(Date())
        val newExpense = Expense(
            id = UUID.randomUUID().toString().replace("-", "").take(10),
            amount = amount,
            category = category,
            note = note.trim(),
            date = todayStr
        )
        viewModelScope.launch {
            repository.addExpense(newExpense)
        }
    }

    fun deleteExpense(id: String) {
        viewModelScope.launch {
            repository.deleteExpense(id)
        }
    }

    fun clearAllExpenses() {
        viewModelScope.launch {
            repository.clearAllExpenses()
        }
    }
}
