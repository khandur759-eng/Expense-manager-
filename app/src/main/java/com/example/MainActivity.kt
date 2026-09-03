package com.example

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.ExpenseScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: ExpenseViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val openAddExpense = intent?.getBooleanExtra("open_add_expense", false) == true

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = Color.White
        ) {
          ExpenseScreen(
            viewModel = viewModel,
            openAddExpenseOnLaunch = openAddExpense,
            onRequestAddPinShortcut = {
              requestPinShortcut()
            }
          )
        }
      }
    }
  }

  private fun requestPinShortcut() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val shortcutManager = getSystemService(ShortcutManager::class.java)
      if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported) {
        val pinIntent = Intent(this, QuickAddExpenseActivity::class.java).apply {
          action = Intent.ACTION_VIEW
        }
        val pinShortcutInfo = ShortcutInfo.Builder(this, "pinned_quick_add_expense")
          .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_add))
          .setShortLabel(getString(R.string.shortcut_add_expense_short))
          .setLongLabel(getString(R.string.shortcut_add_expense_long))
          .setIntent(pinIntent)
          .build()

        shortcutManager.requestPinShortcut(pinShortcutInfo, null)
      } else {
        Toast.makeText(this, "Long press the app icon on home screen to use shortcut", Toast.LENGTH_LONG).show()
      }
    } else {
      Toast.makeText(this, "Long press the app icon on home screen to use shortcut", Toast.LENGTH_LONG).show()
    }
  }
}

