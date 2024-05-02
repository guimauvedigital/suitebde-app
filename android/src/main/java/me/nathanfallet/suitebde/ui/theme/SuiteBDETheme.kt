package me.nathanfallet.suitebde.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val primaryColor = Color(0xFF995DC9)
val primaryVariantColor = Color(0xFFE29464)
val secondaryColor = Color(0xFF03DAC5)

val backgroundColor = Color(0xFFF2F2F7)
val darkBackgroundColor = Color(0xFF000000)

val cardColor = Color(0xFFFFFFFF)
val darkCardColor = Color(0xFF1E1E1E)

@Composable
@Suppress("FunctionName")
fun SuiteBDETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) darkColorScheme(
        primary = primaryColor,
        secondary = secondaryColor,
        background = darkBackgroundColor,
        surfaceVariant = darkCardColor
    ) else lightColorScheme(
        primary = primaryColor,
        secondary = secondaryColor,
        background = backgroundColor,
        surfaceVariant = cardColor
    )
    val shapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        shapes = shapes,
        content = content
    )
}
