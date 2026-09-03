package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Expense
import com.example.model.getCategoryById
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Red600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.util.Formatters
import com.example.viewmodel.ExpenseViewModel

@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier,
    openAddExpenseOnLaunch: Boolean = false,
    onRequestAddPinShortcut: (() -> Unit)? = null
) {
    val salary by viewModel.salary.collectAsStateWithLifecycle()
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()

    var showAddExpense by remember { mutableStateOf(openAddExpenseOnLaunch) }
    var isEditingSalary by remember { mutableStateOf(false) }
    var salaryInput by remember { mutableStateOf("") }
    var showClearAllConfirmation by remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    val totalSpent = remember(expenses) { expenses.sumOf { it.amount } }
    val remainingBalance = salary - totalSpent
    val hasSalary = salary > 0L && !isEditingSalary
    val spentPercentage = if (salary > 0L) {
        (totalSpent.toFloat() / salary.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("expense_screen_content")
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Slate900),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Expense Manager",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Monthly budget overview",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate500
                            )
                        }
                    }

                    // Badge
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Slate50)
                            .border(1.dp, Slate200, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${expenses.size} ${if (expenses.size == 1) "expense" else "expenses"}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate600
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Slate100)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Monthly Salary Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "MONTHLY SALARY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                            color = Slate500
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Set your monthly income to track balance.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate600
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (hasSalary) {
                            // Display Salary + Edit button
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = Formatters.formatInr(salary),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate900,
                                    letterSpacing = (-0.5).sp
                                )

                                Box(
                                    modifier = Modifier
                                        .height(28.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Slate100)
                                        .border(1.dp, Slate200, RoundedCornerShape(14.dp))
                                        .clickable {
                                            salaryInput = salary.toString()
                                            isEditingSalary = true
                                        }
                                        .padding(horizontal = 12.dp)
                                        .testTag("edit_salary_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Edit",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Slate600
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Progress bar
                            val spentPercentText = (spentPercentage * 100).toInt()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Spent $spentPercentText%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate500
                                )
                                Text(
                                    text = "${Formatters.formatInr(totalSpent)} of ${Formatters.formatInr(salary)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate500
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Slate100)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(spentPercentage)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Slate900)
                                )
                            }
                        } else {
                            // Input to enter/update salary
                            Text(
                                text = "Enter Amount",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate700
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 14.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "₹",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Slate400,
                                            modifier = Modifier.padding(end = 6.dp)
                                        )

                                        BasicTextField(
                                            value = salaryInput,
                                            onValueChange = { newValue ->
                                                val clean = newValue.filter { it.isDigit() }
                                                if (clean.length <= 9) {
                                                    salaryInput = clean
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("salary_input"),
                                            textStyle = TextStyle(
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Slate900
                                            ),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true,
                                            cursorBrush = SolidColor(Slate900),
                                            decorationBox = { innerTextField ->
                                                if (salaryInput.isEmpty()) {
                                                    Text(
                                                        text = "0",
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = Slate300
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        val amt = salaryInput.toLongOrNull() ?: 0L
                                        if (amt > 0) {
                                            viewModel.saveSalary(amt)
                                            salaryInput = ""
                                            isEditingSalary = false
                                        }
                                    },
                                    enabled = (salaryInput.toLongOrNull() ?: 0L) > 0,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Slate900,
                                        contentColor = Color.White,
                                        disabledContainerColor = Slate900.copy(alpha = 0.35f),
                                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("save_salary_button")
                                ) {
                                    Text(
                                        text = "Save Salary",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Stat Cards: Total Spent & Remaining Balance (2-column grid)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Spent Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "TOTAL SPENT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                                color = Slate500
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (totalSpent == 0L) "₹0" else Formatters.formatInr(totalSpent),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate900,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${expenses.size} transactions",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate500
                            )
                        }
                    }

                    // Remaining Balance Card
                    val isNegative = remainingBalance < 0
                    val balanceColor = if (isNegative) Red600 else Emerald700
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "REMAINING BALANCE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                                color = Slate500
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (salary == 0L) "₹0" else Formatters.formatInr(remainingBalance),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = balanceColor,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = when {
                                    isNegative -> "Over budget"
                                    salary > 0L -> "Available"
                                    else -> "Set salary to track"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = balanceColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

            // Expenses Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Expenses",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )

                    if (expenses.isNotEmpty()) {
                        Text(
                            text = "Clear all",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate500,
                            modifier = Modifier
                                .clickable { showClearAllConfirmation = true }
                                .padding(4.dp)
                                .testTag("clear_all_expenses_button")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Empty state or Expense List
            if (expenses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                            .padding(vertical = 36.dp, horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Slate50)
                                    .border(1.dp, Slate200, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = Slate400,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "No expenses yet. Tap Add Expense to get started.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate700
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Your transactions will appear here.",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                // List of expenses with clean border container and dividers
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                    ) {
                        Column {
                            expenses.forEachIndexed { index, expense ->
                                ExpenseRowItem(
                                    expense = expense,
                                    onDelete = { expenseToDelete = expense }
                                )
                                if (index < expenses.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(Slate100)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Bottom Add Expense Button
            item {
                Button(
                    onClick = { showAddExpense = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Slate900,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("add_expense_fab_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Expense",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (onRequestAddPinShortcut != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onRequestAddPinShortcut,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Slate50,
                            contentColor = Slate700
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                            .testTag("pin_shortcut_button")
                    ) {
                        Text(
                            text = "Add 'Quick Expense' to Home Screen",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "All data is stored locally on your device",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate400
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }

        // Add Expense Dialog / Full Screen Sheet
        AnimatedVisibility(
            visible = showAddExpense,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            AddExpenseSheet(
                onDismiss = { showAddExpense = false },
                onSave = { amount, category, note ->
                    viewModel.addExpense(amount, category, note)
                    showAddExpense = false
                }
            )
        }

        // Confirmation Dialog for Clear All
        if (showClearAllConfirmation) {
            AlertDialog(
                onDismissRequest = { showClearAllConfirmation = false },
                title = {
                    Text(
                        text = "Clear all expenses?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete all ${expenses.size} expenses? This action cannot be undone.",
                        fontSize = 14.sp,
                        color = Slate600
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showClearAllConfirmation = false
                            viewModel.clearAllExpenses()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red600,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("confirm_clear_all_button")
                    ) {
                        Text(
                            text = "Clear All",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showClearAllConfirmation = false },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("cancel_clear_all_button")
                    ) {
                        Text(
                            text = "Cancel",
                            color = Slate600,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("clear_all_confirmation_dialog")
            )
        }

        // Confirmation Dialog for Single Expense Deletion
        expenseToDelete?.let { targetExpense ->
            AlertDialog(
                onDismissRequest = { expenseToDelete = null },
                title = {
                    Text(
                        text = "Delete expense?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                },
                text = {
                    val noteDescription = if (targetExpense.note.isNotBlank()) " (\"${targetExpense.note}\")" else ""
                    Text(
                        text = "Are you sure you want to delete the ${Formatters.formatInr(targetExpense.amount)} expense for ${targetExpense.category}$noteDescription?",
                        fontSize = 14.sp,
                        color = Slate600
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val id = targetExpense.id
                            expenseToDelete = null
                            viewModel.deleteExpense(id)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red600,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("confirm_delete_expense_button")
                    ) {
                        Text(
                            text = "Delete",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { expenseToDelete = null },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("cancel_delete_expense_button")
                    ) {
                        Text(
                            text = "Cancel",
                            color = Slate600,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("delete_expense_confirmation_dialog")
            )
        }
    }
}

@Composable
private fun ExpenseRowItem(
    expense: Expense,
    onDelete: () -> Unit
) {
    val category = getCategoryById(expense.category)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .testTag("expense_row_${expense.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Category Icon Box
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Slate50)
                .border(1.dp, Slate200, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.label,
                tint = Slate700,
                modifier = Modifier.size(18.dp)
            )
        }

        // Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = Formatters.formatInr(expense.amount),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate50)
                        .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = category.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate600
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (expense.note.isNotBlank()) expense.note else category.label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = "· ${Formatters.formatDate(expense.date)}",
                    fontSize = 11.sp,
                    color = Slate400
                )
            }
        }

        // Remove button
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                .clickable(onClick = onDelete)
                .testTag("delete_expense_${expense.id}"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove expense",
                tint = Slate500,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
