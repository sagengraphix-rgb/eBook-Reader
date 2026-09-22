package com.example.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

enum class ReaderTheme(
    val title: String,
    val backgroundColor: Color,
    val textColor: Color,
    val surfaceColor: Color,
    val accentColor: Color,
    val secondaryTextColor: Color
) {
    LIGHT(
        title = "Light",
        backgroundColor = Color(0xFFFBF9F5),
        textColor = Color(0xFF262626),
        surfaceColor = Color(0xFFF2ECE1),
        accentColor = Color(0xFFB45309),
        secondaryTextColor = Color(0xFF78716C)
    ),
    SEPIA(
        title = "Sepia",
        backgroundColor = Color(0xFFF4ECD8),
        textColor = Color(0xFF382E25),
        surfaceColor = Color(0xFFE9DEBE),
        accentColor = Color(0xFF92400E),
        secondaryTextColor = Color(0xFF7C6E5C)
    ),
    CHARCOAL(
        title = "Charcoal",
        backgroundColor = Color(0xFF1E2022),
        textColor = Color(0xFFE2E8F0),
        surfaceColor = Color(0xFF2B2D31),
        accentColor = Color(0xFFFBBF24),
        secondaryTextColor = Color(0xFF94A3B8)
    ),
    BLACK(
        title = "Night",
        backgroundColor = Color(0xFF0F0F10),
        textColor = Color(0xFFCBD5E1),
        surfaceColor = Color(0xFF1C1D1F),
        accentColor = Color(0xFFF59E0B),
        secondaryTextColor = Color(0xFF64748B)
    )
}

enum class ReaderFont(
    val title: String,
    val fontFamily: FontFamily
) {
    SERIF("Serif", FontFamily.Serif),
    SANS("Sans", FontFamily.SansSerif),
    MONO("Mono", FontFamily.Monospace)
}

enum class ReaderLayoutMode(val title: String) {
    PAGINATED("Paginated"),
    CONTINUOUS("Scroll")
}

data class ReaderPreferences(
    val theme: ReaderTheme = ReaderTheme.SEPIA,
    val font: ReaderFont = ReaderFont.SERIF,
    val fontSizeSp: Float = 18f,
    val lineHeightMultiplier: Float = 1.6f,
    val horizontalMarginDp: Int = 20,
    val verticalMarginDp: Int = 16,
    val layoutMode: ReaderLayoutMode = ReaderLayoutMode.PAGINATED,
    val screenWarmth: Float = 0.0f // 0f to 0.5f amber tint
)
