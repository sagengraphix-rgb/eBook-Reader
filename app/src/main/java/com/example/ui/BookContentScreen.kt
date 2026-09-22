package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FormatAlignLeft
import androidx.compose.material.icons.outlined.ViewDay
import androidx.compose.material.icons.outlined.ViewSidebar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReaderFont
import com.example.data.model.ReaderTheme

/**
 * A composable Screen that renders book content with interactive, customizable
 * text size and margin settings.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookContentScreen(
    title: String = "Pride and Prejudice",
    author: String = "Jane Austen",
    chapterTitle: String = "Chapter 1 — The Netherfield News",
    content: String = DEFAULT_BOOK_CONTENT,
    initialTextSizeSp: Float = 18f,
    initialHorizontalMarginDp: Int = 24,
    initialVerticalMarginDp: Int = 16,
    onNavigateBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var textSizeSp by remember { mutableFloatStateOf(initialTextSizeSp) }
    var horizontalMarginDp by remember { mutableIntStateOf(initialHorizontalMarginDp) }
    var verticalMarginDp by remember { mutableIntStateOf(initialVerticalMarginDp) }
    var currentTheme by remember { mutableStateOf(ReaderTheme.SEPIA) }
    var currentFont by remember { mutableStateOf(ReaderFont.SERIF) }
    var isControlPanelVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("book_content_screen"),
        containerColor = currentTheme.backgroundColor,
        topBar = {
            Surface(
                color = currentTheme.backgroundColor,
                shadowElevation = if (scrollState.value > 10) 2.dp else 0.dp
            ) {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = currentTheme.backgroundColor,
                        titleContentColor = currentTheme.textColor,
                        navigationIconContentColor = currentTheme.textColor,
                        actionIconContentColor = currentTheme.textColor
                    ),
                    navigationIcon = {
                        if (onNavigateBack != null) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier.testTag("back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Navigate back"
                                )
                            }
                        }
                    },
                    title = {
                        Column {
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = currentTheme.textColor
                            )
                            Text(
                                text = author,
                                fontSize = 12.sp,
                                color = currentTheme.secondaryTextColor
                            )
                        }
                    },
                    actions = {
                        // Quick badge showing current text size & margin
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = currentTheme.surfaceColor,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${textSizeSp.toInt()}sp · ${horizontalMarginDp}dp",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = currentTheme.accentColor
                                )
                            }
                        }

                        // Toggle settings control panel
                        IconButton(
                            onClick = { isControlPanelVisible = !isControlPanelVisible },
                            modifier = Modifier.testTag("settings_toggle_btn")
                        ) {
                            Icon(
                                imageVector = if (isControlPanelVisible) Icons.Outlined.Close else Icons.Default.Tune,
                                contentDescription = if (isControlPanelVisible) "Close layout settings" else "Customize text & margins",
                                tint = if (isControlPanelVisible) currentTheme.accentColor else currentTheme.textColor
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // Main Book Content Column with customizable margins and text size
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(
                        start = horizontalMarginDp.dp,
                        end = horizontalMarginDp.dp,
                        top = verticalMarginDp.dp,
                        bottom = verticalMarginDp.dp + if (isControlPanelVisible) 260.dp else 40.dp
                    )
                    .testTag("book_scrollable_container")
            ) {
                // Chapter Title Header
                Text(
                    text = chapterTitle,
                    fontFamily = currentFont.fontFamily,
                    fontSize = (textSizeSp + 6).sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.accentColor,
                    lineHeight = (textSizeSp + 10).sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                HorizontalDivider(
                    color = currentTheme.secondaryTextColor.copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Paragraphs rendered with customized text size and line spacing
                val paragraphs = content.split("\n\n").filter { it.isNotBlank() }
                paragraphs.forEachIndexed { index, paragraph ->
                    Text(
                        text = paragraph.trim(),
                        fontFamily = currentFont.fontFamily,
                        fontSize = textSizeSp.sp,
                        lineHeight = (textSizeSp * 1.6f).sp,
                        color = currentTheme.textColor,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .testTag(if (index == 0) "book_content_text" else "paragraph_$index")
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // End of chapter marker
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "— End of Section —",
                        fontStyle = FontStyle.Italic,
                        fontSize = (textSizeSp - 3).coerceAtLeast(11f).sp,
                        color = currentTheme.secondaryTextColor
                    )
                }
            }

            // Customizable Settings Bottom Panel
            AnimatedVisibility(
                visible = isControlPanelVisible,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            ) {
                CustomizationControlSheet(
                    textSizeSp = textSizeSp,
                    horizontalMarginDp = horizontalMarginDp,
                    verticalMarginDp = verticalMarginDp,
                    currentTheme = currentTheme,
                    currentFont = currentFont,
                    onTextSizeChange = { textSizeSp = it },
                    onHorizontalMarginChange = { horizontalMarginDp = it },
                    onVerticalMarginChange = { verticalMarginDp = it },
                    onThemeChange = { currentTheme = it },
                    onFontChange = { currentFont = it },
                    onClose = { isControlPanelVisible = false }
                )
            }
        }
    }
}

/**
 * Interactive settings panel dedicated to customizing text size, horizontal margins,
 * vertical margins, typography, and themes with live visual feedback.
 */
@Composable
private fun CustomizationControlSheet(
    textSizeSp: Float,
    horizontalMarginDp: Int,
    verticalMarginDp: Int,
    currentTheme: ReaderTheme,
    currentFont: ReaderFont,
    onTextSizeChange: (Float) -> Unit,
    onHorizontalMarginChange: (Int) -> Unit,
    onVerticalMarginChange: (Int) -> Unit,
    onThemeChange: (ReaderTheme) -> Unit,
    onFontChange: (ReaderFont) -> Unit,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .testTag("customization_panel"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Panel Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = currentTheme.accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Text & Margin Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = currentTheme.textColor
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Close panel",
                        tint = currentTheme.secondaryTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Text Size Customization Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = null,
                        tint = currentTheme.secondaryTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Text Size",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = currentTheme.secondaryTextColor
                    )
                }
                Text(
                    text = "${textSizeSp.toInt()} sp",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.accentColor
                )
            }

            // Text size steppers + continuous slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onTextSizeChange((textSizeSp - 2f).coerceAtLeast(12f)) },
                    enabled = textSizeSp > 12f,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("btn_decrease_font")
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease text size",
                        tint = currentTheme.textColor
                    )
                }

                Slider(
                    value = textSizeSp,
                    onValueChange = onTextSizeChange,
                    valueRange = 12f..36f,
                    steps = 11,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .testTag("text_size_slider"),
                    colors = SliderDefaults.colors(
                        thumbColor = currentTheme.accentColor,
                        activeTrackColor = currentTheme.accentColor,
                        inactiveTrackColor = currentTheme.secondaryTextColor.copy(alpha = 0.2f)
                    )
                )

                IconButton(
                    onClick = { onTextSizeChange((textSizeSp + 2f).coerceAtMost(36f)) },
                    enabled = textSizeSp < 36f,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("btn_increase_font")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase text size",
                        tint = currentTheme.textColor
                    )
                }
            }

            // Quick text size presets
            val sizePresets = listOf(
                14f to "Small",
                18f to "Normal",
                24f to "Large",
                30f to "Huge"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                sizePresets.forEach { (size, label) ->
                    val isSelected = textSizeSp.toInt() == size.toInt()
                    FilterChip(
                        modifier = Modifier.weight(1f),
                        selected = isSelected,
                        onClick = { onTextSizeChange(size) },
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
                            selectedContainerColor = currentTheme.accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = currentTheme.textColor,
                            containerColor = currentTheme.backgroundColor,
                            labelColor = currentTheme.secondaryTextColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Horizontal Margin Settings Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.ViewSidebar,
                        contentDescription = null,
                        tint = currentTheme.secondaryTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Horizontal Margin",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = currentTheme.secondaryTextColor
                    )
                }
                Text(
                    text = "${horizontalMarginDp} dp",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.accentColor
                )
            }

            // Margin Presets Chips
            val marginPresets = listOf(
                12 to "Compact",
                24 to "Standard",
                36 to "Wide",
                48 to "Spacious"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                marginPresets.forEach { (marginVal, label) ->
                    val isSelected = horizontalMarginDp == marginVal
                    FilterChip(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("margin_chip_${label.lowercase()}"),
                        selected = isSelected,
                        onClick = { onHorizontalMarginChange(marginVal) },
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
                            selectedContainerColor = currentTheme.accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = currentTheme.textColor,
                            containerColor = currentTheme.backgroundColor,
                            labelColor = currentTheme.secondaryTextColor
                        )
                    )
                }
            }

            // Continuous Margin Slider
            Slider(
                value = horizontalMarginDp.toFloat(),
                onValueChange = { onHorizontalMarginChange(it.toInt()) },
                valueRange = 8f..64f,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("margin_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = currentTheme.accentColor,
                    activeTrackColor = currentTheme.accentColor,
                    inactiveTrackColor = currentTheme.secondaryTextColor.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Typography & Theme Quick Switchers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Font family toggles
                ReaderFont.values().forEach { font ->
                    val isSelected = currentFont == font
                    FilterChip(
                        modifier = Modifier.weight(1f),
                        selected = isSelected,
                        onClick = { onFontChange(font) },
                        label = {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = font.title,
                                    fontFamily = font.fontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = currentTheme.accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = currentTheme.textColor,
                            containerColor = currentTheme.backgroundColor,
                            labelColor = currentTheme.secondaryTextColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Themes selection row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReaderTheme.values().forEach { theme ->
                    val isSelected = currentTheme == theme
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) theme.accentColor else Color.Gray.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onThemeChange(theme) },
                        color = theme.backgroundColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = theme.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = theme.textColor
                            )
                        }
                    }
                }
            }
        }
    }
}

private const val DEFAULT_BOOK_CONTENT = """It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.

However little known the feelings or views of such a man may be on his first entering a neighbourhood, this truth is so well fixed in the minds of the surrounding families, that he is considered the rightful property of some one or other of their daughters.

"My dear Mr. Bennet," said his lady to him one day, "have you heard that Netherfield Park is let at last?"

Mr. Bennet replied that he had not.

"But it is," returned she; "for Mrs. Long has just been here, and she told me all about it."

Mr. Bennet made no answer.

"Do you not want to know who has taken it?" cried his wife impatiently.

"You want to tell me, and I have no objection to hearing it."

This was invitation enough.

"Why, my dear, you must know, Mrs. Long says that Netherfield is taken by a young man of large fortune from the north of England; that he came down on Monday in a chaise and four to see the place, and was so much delighted with it, that he agreed with Mr. Morris immediately; that he is to take possession before Michaelmas, and some of his servants are to be in the house by the end of next week."

"What is his name?"

"Bingley."

"Is he married or single?"

"Oh! Single, my dear, to be sure! A single man of large fortune; four or five thousand a year. What a fine thing for our girls!"

"How so? How can it affect them?"

"My dear Mr. Bennet," replied his wife, "how can you be so tiresome! You must know that I am thinking of his marrying one of them."

"Is that his design in settling here?"

"Design! Nonsense, how can you talk so! But it is very likely that he may fall in love with one of them, and therefore you must visit him as soon as he comes."

"I see no occasion for that. You and the girls may go, or you may send them by themselves, which perhaps will be still better, for as you are as handsome as any of them, Mr. Bingley might like you the best of the party."

"My dear, you flatter me. I certainly have had my share of beauty, but I do not pretend to be anything extraordinary now. When a woman has five grown-up daughters, she ought to give over thinking of her own beauty."

"In such cases, a woman has not often much beauty to think of."

"But, my dear, you must indeed go and see Mr. Bennet."

Mr. Bennet was so odd a mixture of quick parts, sarcastic humour, reserve, and caprice, that the experience of three-and-twenty years had been insufficient to make his wife understand his character. Her mind was less difficult to develop. She was a woman of mean understanding, little information, and uncertain temper. When she was discontented, she fancied herself nervous. The business of her life was to get her daughters married; its solace was visiting and news."""

@Preview(showBackground = true)
@Composable
fun BookContentScreenPreview() {
    BookContentScreen()
}
