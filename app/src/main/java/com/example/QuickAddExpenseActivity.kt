package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.ui.AddExpenseSheet
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ExpenseViewModel

class QuickAddExpenseActivity : ComponentActivity() {
    private val viewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                AddExpenseSheet(
                    modifier = Modifier.fillMaxSize(),
                    onDismiss = { finish() },
                    onSave = { amount, category, note ->
                        viewModel.addExpense(amount, category, note)
                        Toast.makeText(this, "Expense recorded", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                )
            }
        }
    }
}
