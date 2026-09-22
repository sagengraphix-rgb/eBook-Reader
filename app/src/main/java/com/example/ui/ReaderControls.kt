package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.BrightnessMedium
import androidx.compose.material.icons.outlined.FormatAlignLeft
import androidx.compose.material.icons.outlined.FormatLineSpacing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReaderFont
import com.example.data.model.ReaderLayoutMode
import com.example.data.model.ReaderPreferences
import com.example.data.model.ReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsSheet(
    preferences: ReaderPreferences,
    onDismiss: () -> Unit,
    onThemeSelect: (ReaderTheme) -> Unit,
    onFontSelect: (ReaderFont) -> Unit,
    onAdjustFontSize: (Float) -> Unit,
    onLineHeightSelect: (Float) -> Unit,
    onMarginSelect: (Int) -> Unit,
    onLayoutModeSelect: (ReaderLayoutMode) -> Unit,
    onWarmthChange: (Float) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = preferences.theme.surfaceColor,
        contentColor = preferences.theme.textColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .padding(bottom = 28.dp)
        ) {
            Text(
                text = "Reading Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = preferences.theme.textColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Themes selection
            Text(
                text = "Theme",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = preferences.theme.secondaryTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReaderTheme.values().forEach { theme ->
                    val isSelected = preferences.theme == theme
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) theme.accentColor else Color.Gray.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onThemeSelect(theme) },
                        color = theme.backgroundColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = theme.title,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = theme.textColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Font size & Family
            Text(
                text = "Typography",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = preferences.theme.secondaryTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Font Family Chips
                ReaderFont.values().forEach { font ->
                    val isSelected = preferences.font == font
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFontSelect(font) },
                        label = {
                            Text(
                                text = font.title,
                                fontFamily = font.fontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = preferences.theme.accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = preferences.theme.textColor,
                            containerColor = preferences.theme.backgroundColor,
                            labelColor = preferences.theme.secondaryTextColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Font Size Stepper
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(preferences.theme.backgroundColor)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onAdjustFontSize(-2f) },
                    enabled = preferences.fontSizeSp > 13f
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease font size",
                        tint = preferences.theme.textColor
                    )
                }

                Text(
                    text = "${preferences.fontSizeSp.toInt()} sp",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = preferences.theme.textColor
                )

                IconButton(
                    onClick = { onAdjustFontSize(2f) },
                    enabled = preferences.fontSizeSp < 32f
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase font size",
                        tint = preferences.theme.textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Margins Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Page Margins",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = preferences.theme.secondaryTextColor
                )
                Text(
                    text = "${preferences.horizontalMarginDp} dp",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = preferences.theme.accentColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Margin Presets
            val marginPresets = listOf(
                12 to "Compact",
                20 to "Standard",
                32 to "Wide",
                44 to "Spacious"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                marginPresets.forEach { (marginVal, label) ->
                    val isSelected = preferences.horizontalMarginDp == marginVal
                    FilterChip(
                        modifier = Modifier.weight(1f),
                        selected = isSelected,
                        onClick = { onMarginSelect(marginVal) },
                        label = {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = preferences.theme.accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = preferences.theme.textColor,
                            containerColor = preferences.theme.backgroundColor,
                            labelColor = preferences.theme.secondaryTextColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Margin Continuous Slider
            Slider(
                value = preferences.horizontalMarginDp.toFloat(),
                onValueChange = { onMarginSelect(it.toInt()) },
                valueRange = 8f..56f,
                colors = SliderDefaults.colors(
                    thumbColor = preferences.theme.accentColor,
                    activeTrackColor = preferences.theme.accentColor,
                    inactiveTrackColor = preferences.theme.secondaryTextColor.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Layout Mode
            Text(
                text = "Layout Mode",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = preferences.theme.secondaryTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReaderLayoutMode.values().forEach { mode ->
                    val isSelected = preferences.layoutMode == mode
                    FilterChip(
                        modifier = Modifier.weight(1f),
                        selected = isSelected,
                        onClick = { onLayoutModeSelect(mode) },
                        label = {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text(mode.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = preferences.theme.accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = preferences.theme.textColor,
                            containerColor = preferences.theme.backgroundColor,
                            labelColor = preferences.theme.secondaryTextColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Screen Warmth (Night light tint slider)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.BrightnessMedium,
                        contentDescription = null,
                        tint = preferences.theme.secondaryTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Warmth Tint",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = preferences.theme.secondaryTextColor
                    )
                }
                Text(
                    text = "${(preferences.screenWarmth * 200).toInt()}%",
                    fontSize = 12.sp,
                    color = preferences.theme.secondaryTextColor
                )
            }

            Slider(
                value = preferences.screenWarmth,
                onValueChange = onWarmthChange,
                valueRange = 0f..0.40f,
                colors = SliderDefaults.colors(
                    thumbColor = preferences.theme.accentColor,
                    activeTrackColor = preferences.theme.accentColor,
                    inactiveTrackColor = preferences.theme.secondaryTextColor.copy(alpha = 0.2f)
                )
            )
        }
    }
}
