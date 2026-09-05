package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = Slate100,
    onPrimary = Slate900,
    surface = Slate900,
    onSurface = Slate100,
    background = Slate900,
    onBackground = Slate100,
    outline = Slate700
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Slate900,
    onPrimary = Color.White,
    surface = Color.White,
    onSurface = Slate900,
    background = Color.White,
    onBackground = Slate900,
    outline = BorderCrisp,
    surfaceVariant = Slate50,
    onSurfaceVariant = Slate700
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

