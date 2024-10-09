package com.suitebde.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import software.guimauve.ui.theme.DefaultTheme

val primaryColor = Color(0xFF995DC9)
val primaryVariantColor = Color(0xFFE29464)
val secondaryColor = Color(0xFF03DAC5)

@Composable
@Suppress("FunctionName")
fun SuiteBDETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) darkColorScheme(
        primary = primaryColor,
        secondary = secondaryColor,
        background = DefaultTheme.darkBackgroundColor,
        surfaceVariant = DefaultTheme.darkCardColor
    ) else lightColorScheme(
        primary = primaryColor,
        secondary = secondaryColor,
        background = DefaultTheme.backgroundColor,
        surfaceVariant = DefaultTheme.cardColor
    )
    MaterialTheme(
        colorScheme = colors,
        shapes = DefaultTheme.shapes,
        content = content
    )
}
