package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Expense
import com.example.model.ExpenseCategories
import com.example.model.ExpenseCategory
import com.example.model.getCategoryById
import com.example.ui.theme.BorderCrisp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.util.Formatters

data class CategorySpendData(
    val category: ExpenseCategory,
    val totalAmount: Long,
    val count: Int,
    val percentage: Float
)

@Composable
fun CategorySpendingBarChart(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    val totalSpent = remember(expenses) { expenses.sumOf { it.amount } }

    // Aggregate spending by category
    val categoryDataList = remember(expenses, totalSpent) {
        val expenseMap = expenses.groupBy { it.category }
        ExpenseCategories.map { cat ->
            val catExpenses = expenseMap[cat.id] ?: emptyList()
            val sum = catExpenses.sumOf { it.amount }
            val count = catExpenses.size
            val pct = if (totalSpent > 0L) (sum.toFloat() / totalSpent.toFloat()) * 100f else 0f
            CategorySpendData(
                category = cat,
                totalAmount = sum,
                count = count,
                percentage = pct
            )
        }
    }

    val maxAmount = remember(categoryDataList) {
        val maxCat = categoryDataList.maxOfOrNull { it.totalAmount } ?: 0L
        if (maxCat > 0L) maxCat else 1000L
    }

    // Selected category for D3/Recharts-style interactive tooltip inspection
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    val selectedData = remember(selectedCategoryId, categoryDataList) {
        categoryDataList.find { it.category.id == selectedCategoryId }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x120F172A),
                spotColor = Color(0x1C0F172A)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(1.2.dp, BorderCrisp, RoundedCornerShape(22.dp))
            .padding(20.dp)
            .testTag("category_spending_bar_chart")
    ) {
        Column {
            // Header: Title
            Text(
                text = "MONTHLY SPENDING",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = Slate600
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Recharts-Style Interactive Tooltip Box
            AnimatedVisibility(
                visible = selectedData != null && selectedData.totalAmount > 0L,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                if (selectedData != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Slate900)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .testTag("chart_tooltip_box")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(selectedData.category.iconColor)
                                )
                                Column {
                                    Text(
                                        text = selectedData.category.label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Slate200
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = Formatters.formatInr(selectedData.totalAmount),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "•  ${String.format("%.1f", selectedData.percentage)}% (${selectedData.count} txns)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Slate300
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.12f))
                                    .clickable { selectedCategoryId = null },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close tooltip",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (expenses.isEmpty()) {
                // Empty State with faint preview bars
                EmptyChartPreview()
            } else {
                // Interactive Bar Chart Canvas & Bars
                InteractiveBarChart(
                    dataList = categoryDataList,
                    maxAmount = maxAmount,
                    selectedCategoryId = selectedCategoryId,
                    onSelectCategory = { id ->
                        selectedCategoryId = if (selectedCategoryId == id) null else id
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Category Breakdown Progress Bars (Detailed Summary)
                CategoryBreakdownDetails(
                    dataList = categoryDataList.filter { it.totalAmount > 0L }.sortedByDescending { it.totalAmount },
                    totalSpent = totalSpent,
                    selectedCategoryId = selectedCategoryId,
                    onSelectCategory = { id ->
                        selectedCategoryId = if (selectedCategoryId == id) null else id
                    }
                )
            }
        }
    }
}

@Composable
private fun InteractiveBarChart(
    dataList: List<CategorySpendData>,
    maxAmount: Long,
    selectedCategoryId: String?,
    onSelectCategory: (String) -> Unit
) {
    val chartHeight = 160.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        // Upper Chart Canvas with Gridlines & Animated Bars
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
                .drawBehind {
                    // Recharts-style horizontal dashed gridlines (0%, 50%, 100%)
                    val strokeWidth = 1.dp.toPx()
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    val gridColor = Color(0xFFCBD5E1)

                    // 100% line
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth,
                        pathEffect = pathEffect
                    )

                    // 50% line
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, size.height * 0.5f),
                        end = Offset(size.width, size.height * 0.5f),
                        strokeWidth = strokeWidth,
                        pathEffect = pathEffect
                    )

                    // Baseline (0%)
                    drawLine(
                        color = Color(0xFF94A3B8),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }
        ) {
            // Bars row
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                dataList.forEach { item ->
                    val isSelected = selectedCategoryId == item.category.id
                    val isAnySelected = selectedCategoryId != null
                    val hasSpend = item.totalAmount > 0L

                    val targetFraction = if (maxAmount > 0L && hasSpend) {
                        (item.totalAmount.toFloat() / maxAmount.toFloat()).coerceIn(0.04f, 1f)
                    } else {
                        0.03f // tiny visible baseline nub
                    }

                    val animatedFraction by animateFloatAsState(
                        targetValue = targetFraction,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "bar_height_${item.category.id}"
                    )

                    val barAlpha = when {
                        !hasSpend -> 0.35f
                        !isAnySelected || isSelected -> 1.0f
                        else -> 0.45f
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (hasSpend) {
                                    onSelectCategory(item.category.id)
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // Value label above bar (visible if selected or highest)
                        if (isSelected && hasSpend) {
                            Text(
                                text = formatCompactInr(item.totalAmount),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = item.category.iconColor,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // Bar Container: Track + Fill (Recharts background pattern)
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .weight(1f, fill = false)
                                .height(chartHeight * animatedFraction)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(
                                    if (hasSpend) {
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                item.category.iconColor.copy(alpha = barAlpha),
                                                item.category.iconColor.copy(alpha = barAlpha * 0.75f)
                                            )
                                        )
                                    } else {
                                        Brush.verticalGradient(
                                            colors = listOf(Slate200, Slate100)
                                        )
                                    }
                                )
                                .then(
                                    if (isSelected) {
                                        Modifier.border(
                                            width = 2.dp,
                                            color = Slate900,
                                            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                        )
                                    } else Modifier
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // X-Axis Labels & Icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dataList.forEach { item ->
                val isSelected = selectedCategoryId == item.category.id
                val hasSpend = item.totalAmount > 0L

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (hasSpend) {
                                onSelectCategory(item.category.id)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(
                                if (isSelected) item.category.iconColor else item.category.backgroundColor
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.category.icon,
                            contentDescription = item.category.label,
                            tint = if (isSelected) Color.White else item.category.iconColor,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.category.id,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) Slate900 else if (hasSpend) Slate700 else Slate500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdownDetails(
    dataList: List<CategorySpendData>,
    totalSpent: Long,
    selectedCategoryId: String?,
    onSelectCategory: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BREAKDOWN SUMMARY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = Slate600
            )
            Text(
                text = "${dataList.size} ${if (dataList.size == 1) "category" else "categories"}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate700
            )
        }

        dataList.forEach { item ->
            val isSelected = selectedCategoryId == item.category.id
            val animatedProgress by animateFloatAsState(
                targetValue = (item.percentage / 100f).coerceIn(0f, 1f),
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "progress_${item.category.id}"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) Slate50 else Color.Transparent)
                    .clickable { onSelectCategory(item.category.id) }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Category Icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(item.category.backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.category.icon,
                        contentDescription = null,
                        tint = item.category.iconColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Title + Progress Bar
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.category.label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate800
                        )
                        Text(
                            text = Formatters.formatInr(item.totalAmount),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Progress Track & Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Slate200)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(3.dp))
                                .background(item.category.iconColor)
                        )
                    }
                }

                // Percentage Badge
                Box(
                    modifier = Modifier
                        .width(46.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Slate100)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(6.dp))
                        .padding(vertical = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${String.format("%.0f", item.percentage)}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyChartPreview() {
    val dummyFractions = listOf(0.45f, 0.7f, 0.3f, 0.85f, 0.5f, 0.25f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Faint Ghost Bars Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                    val gridColor = Color(0xFFF1F5F9)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, size.height * 0.5f),
                        end = Offset(size.width, size.height * 0.5f),
                        strokeWidth = strokeWidth,
                        pathEffect = pathEffect
                    )
                    drawLine(
                        color = Color(0xFFE2E8F0),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                ExpenseCategories.zip(dummyFractions).forEach { (cat, frac) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .width(26.dp)
                                .height(100.dp * frac)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(cat.iconColor.copy(alpha = 0.18f))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Categories faint labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExpenseCategories.forEach { cat ->
                Text(
                    text = cat.id,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate400,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Friendly Info Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Slate50)
                .border(1.2.dp, BorderCrisp, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null,
                    tint = Slate600,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "No expenses recorded this month yet. Add expenses to visualize category spending.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate700,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

private fun formatCompactInr(amount: Long): String {
    return when {
        amount >= 100_000 -> "₹${amount / 100_000}L"
        amount >= 1_000 -> "₹${amount / 1_000}k"
        else -> "₹$amount"
    }
}
