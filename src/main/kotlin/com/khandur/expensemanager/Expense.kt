package com.khandur.expensemanager

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Simple data model representing an expense entry.
 */
data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val category: String = "General",
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    val notes: String? = null
)

/**
 * Lightweight in-memory repository for Expense objects.
 * Suitable for prototyping or unit tests. Replace with a proper
 * persistence layer (Room, SQLite, network, etc.) for production.
 */
object ExpenseRepository {
    private val expenses = mutableListOf<Expense>()

    fun add(expense: Expense) {
        expenses += expense
    }

    fun remove(id: String): Boolean = expenses.removeIf { it.id == id }

    fun getAll(): List<Expense> = expenses.toList()

    fun findById(id: String): Expense? = expenses.find { it.id == id }

    fun totalAmount(): Double = expenses.sumOf { it.amount }

    fun totalByCategory(): Map<String, Double> =
        expenses.groupBy { it.category }
            .mapValues { (_, list) -> list.sumOf { it.amount } }

    fun clear() = expenses.clear()
}

/**
 * Helper to generate some sample expenses for demos or tests.
 */
fun sampleExpenses(): List<Expense> = listOf(
    Expense(title = "Coffee", amount = 3.50, category = "Food"),
    Expense(title = "Groceries", amount = 45.20, category = "Food"),
    Expense(title = "Taxi", amount = 12.00, category = "Transport"),
    Expense(title = "Book", amount = 15.99, category = "Education")
)

/**
 * Small CLI-style demo when run with kotlin-jvm (not Android).
 */
fun main() {
    println("Expense Manager demo")
    ExpenseRepository.clear()
    sampleExpenses().forEach { ExpenseRepository.add(it) }

    ExpenseRepository.getAll().forEach { e ->
        println("- ${'$'}{e.title}: ${'$'}{e.amount} (${e.category})")
    }

    println("Total: ${'$'}{ExpenseRepository.totalAmount()}")
    println("By category: ${'$'}{ExpenseRepository.totalByCategory()}")
}
