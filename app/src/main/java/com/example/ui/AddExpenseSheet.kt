package com.example.ui

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExpenseCategories
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.util.Formatters

@Composable
fun AddExpenseSheet(
    onDismiss: () -> Unit,
    onSave: (amount: Long, category: String, note: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var amountInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Food") }
    var noteInput by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val amountLong = amountInput.toLongOrNull() ?: 0L
    val canSave = amountLong > 0L

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ADD EXPENSE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = Slate500
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Enter Amount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate50)
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                        .clickable(onClick = onDismiss)
                        .testTag("close_add_expense_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Slate700,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Slate100)
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 20.dp)
            ) {
                // Amount Input
                Text(
                    text = "Amount (₹)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate700
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "₹",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate400,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        BasicTextField(
                            value = amountInput,
                            onValueChange = { newValue ->
                                val clean = newValue.filter { it.isDigit() }
                                if (clean.length <= 9) {
                                    amountInput = clean
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester)
                                .testTag("expense_amount_input"),
                            textStyle = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate900
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            cursorBrush = SolidColor(Slate900),
                            decorationBox = { innerTextField ->
                                if (amountInput.isEmpty()) {
                                    Text(
                                        text = "0",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Slate300
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Select Category
                Text(
                    text = "Select Category",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate700
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Grid of categories (2 columns)
                val categories = ExpenseCategories
                for (i in categories.indices step 2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val cat1 = categories[i]
                        CategoryButton(
                            category = cat1,
                            isSelected = selectedCategory == cat1.id,
                            onClick = { selectedCategory = cat1.id },
                            modifier = Modifier.weight(1f)
                        )

                        if (i + 1 < categories.size) {
                            val cat2 = categories[i + 1]
                            CategoryButton(
                                category = cat2,
                                isSelected = selectedCategory == cat2.id,
                                onClick = { selectedCategory = cat2.id },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Note input
                Text(
                    text = "Note (optional)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate700
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("expense_note_input"),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate900
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(Slate900),
                        decorationBox = { innerTextField ->
                            if (noteInput.isEmpty()) {
                                Text(
                                    text = "e.g. Groceries, Fuel, Rent",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Slate400
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // Bottom action buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Slate100)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Slate50,
                        contentColor = Slate700
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                        .testTag("cancel_add_expense_button")
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Save Expense
                Button(
                    onClick = {
                        if (canSave) {
                            onSave(amountLong, selectedCategory, noteInput)
                        }
                    },
                    enabled = canSave,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Slate900,
                        contentColor = Color.White,
                        disabledContainerColor = Slate900.copy(alpha = 0.35f),
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .testTag("save_expense_button")
                ) {
                    Text(
                        text = if (amountLong > 0) "Save Expense · ${Formatters.formatInr(amountLong)}" else "Save Expense",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryButton(
    category: com.example.model.ExpenseCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) Slate900 else Color.White
    val borderColor = if (isSelected) Slate900 else Slate200
    val textColor = if (isSelected) Color.White else Slate700
    val iconColor = if (isSelected) Color.White else Slate600

    Row(
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
            .testTag("category_button_${category.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = category.label,
            tint = iconColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = category.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            maxLines = 1
        )
    }
}
