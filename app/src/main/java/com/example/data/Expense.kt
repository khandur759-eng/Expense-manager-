package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey val id: String,
    val amount: Long,
    val category: String,
    val note: String,
    val date: String // e.g. "2026-09-02"
)
