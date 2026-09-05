package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ExpenseCategory(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val backgroundColor: Color = Color(0xFFF1F5F9),
    val iconColor: Color = Color(0xFF475569)
)

val ExpenseCategories = listOf(
    ExpenseCategory(
        id = "Food",
        label = "Food & Dining",
        icon = Icons.Default.Restaurant,
        backgroundColor = Color(0xFFE0F2FE),
        iconColor = Color(0xFF2563EB)
    ),
    ExpenseCategory(
        id = "Transport",
        label = "Transportation",
        icon = Icons.Default.DirectionsBus,
        backgroundColor = Color(0xFFE8F5E9),
        iconColor = Color(0xFF059669)
    ),
    ExpenseCategory(
        id = "Shopping",
        label = "Shopping",
        icon = Icons.Default.ShoppingBag,
        backgroundColor = Color(0xFFF3E8FF),
        iconColor = Color(0xFF9333EA)
    ),
    ExpenseCategory(
        id = "Bills",
        label = "Bills & Utilities",
        icon = Icons.Default.Receipt,
        backgroundColor = Color(0xFFFFEDD5),
        iconColor = Color(0xFFEA580C)
    ),
    ExpenseCategory(
        id = "Rent",
        label = "Rent",
        icon = Icons.Default.Home,
        backgroundColor = Color(0xFFFEF3C7),
        iconColor = Color(0xFFD97706)
    ),
    ExpenseCategory(
        id = "Other",
        label = "Others",
        icon = Icons.Default.MoreHoriz,
        backgroundColor = Color(0xFFF1F5F9),
        iconColor = Color(0xFF64748B)
    )
)

fun getCategoryById(id: String): ExpenseCategory {
    return ExpenseCategories.find { it.id.equals(id, ignoreCase = true) } ?: ExpenseCategories.last()
}

