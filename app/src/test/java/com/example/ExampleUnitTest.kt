package com.example

import com.example.data.Expense
import com.example.model.ExpenseCategories
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testCategorySpendingAggregation() {
    val sampleExpenses = listOf(
      Expense(id = "1", amount = 1200L, category = "Food", note = "Dinner", date = "2026-09-01"),
      Expense(id = "2", amount = 800L, category = "Food", note = "Groceries", date = "2026-09-02"),
      Expense(id = "3", amount = 500L, category = "Transport", note = "Metro", date = "2026-09-03"),
      Expense(id = "4", amount = 2500L, category = "Shopping", note = "Shoes", date = "2026-09-04")
    )

    val totalSpent = sampleExpenses.sumOf { it.amount }
    assertEquals(5000L, totalSpent)

    val grouped = sampleExpenses.groupBy { it.category }
    val foodTotal = grouped["Food"]?.sumOf { it.amount } ?: 0L
    val shoppingTotal = grouped["Shopping"]?.sumOf { it.amount } ?: 0L
    val transportTotal = grouped["Transport"]?.sumOf { it.amount } ?: 0L

    assertEquals(2000L, foodTotal)
    assertEquals(2500L, shoppingTotal)
    assertEquals(500L, transportTotal)

    val foodPct = (foodTotal.toFloat() / totalSpent.toFloat()) * 100f
    val shoppingPct = (shoppingTotal.toFloat() / totalSpent.toFloat()) * 100f

    assertEquals(40.0f, foodPct, 0.01f)
    assertEquals(50.0f, shoppingPct, 0.01f)
  }
}

