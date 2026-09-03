package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

data class ExpenseCategory(
    val id: String,
    val label: String,
    val icon: ImageVector
)

val ExpenseCategories = listOf(
    ExpenseCategory("Food", "Food & Dining", Icons.Default.Restaurant),
    ExpenseCategory("Transport", "Transportation", Icons.Default.DirectionsBus),
    ExpenseCategory("Shopping", "Shopping", Icons.Default.ShoppingBag),
    ExpenseCategory("Bills", "Bills & Utilities", Icons.Default.Receipt),
    ExpenseCategory("Rent", "Rent", Icons.Default.Home),
    ExpenseCategory("Other", "Others", Icons.Default.MoreHoriz)
)

fun getCategoryById(id: String): ExpenseCategory {
    return ExpenseCategories.find { it.id.equals(id, ignoreCase = true) } ?: ExpenseCategories.last()
}
