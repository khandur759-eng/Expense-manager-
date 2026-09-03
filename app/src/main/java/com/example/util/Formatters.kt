package com.example.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

object Formatters {
    private val inrFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    fun formatInr(amount: Long): String {
        return inrFormat.format(amount)
    }

    fun formatDate(dateStr: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
            val date = parser.parse(dateStr)
            if (date != null) formatter.format(date) else dateStr
        } catch (e: Exception) {
            dateStr
        }
    }
}
