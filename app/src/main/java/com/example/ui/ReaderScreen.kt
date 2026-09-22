package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FormatColorText
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReaderLayoutMode
import com.example.viewmodel.ReaderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val annotations by viewModel.annotations.collectAsState()
    val isBookmarked by viewModel.isCurrentPageBookmarked.collectAsState()

    val prefs = uiState.preferences
    val currentChapter = uiState.chapters.getOrNull(uiState.currentChapterIndex)
    val pageCount = maxOf(1, uiState.chapterPages.size)
    val currentPage = uiState.currentPageIndex

    var quickSelectedWord by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(prefs.theme.backgroundColor)
            .testTag("reader_screen")
    ) {
        // Main reading content container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = prefs.horizontalMarginDp.dp)
        ) {
            // Reserve space for top bar if visible, or subtle padding
            Spacer(modifier = Modifier.height(if (uiState.isToolbarsVisible) 76.dp else 28.dp))

            // Reader Area
            if (prefs.layoutMode == ReaderLayoutMode.PAGINATED) {
                // Paginated reading mode with left/right page flip and center toolbar toggle
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    val pageText = uiState.chapterPages.getOrNull(currentPage) ?: "Loading chapter..."

                    // Content text
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 8.dp)
                    ) {
                        // Chapter subtitle header on first page
                        if (currentPage == 0 && currentChapter != null) {
                            Text(
                                text = currentChapter.title,
                                fontFamily = prefs.font.fontFamily,
                                fontSize = (prefs.fontSizeSp + 4).sp,
                                fontWeight = FontWeight.Bold,
                                color = prefs.theme.accentColor,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        Text(
                            text = pageText,
                            fontFamily = prefs.font.fontFamily,
                            fontSize = prefs.fontSizeSp.sp,
                            lineHeight = (prefs.fontSizeSp * prefs.lineHeightMultiplier).sp,
                            color = prefs.theme.textColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = { offset ->
                                            // Extract tapped word for instant dictionary / highlight
                                            val words = pageText.split("\\s+".toRegex())
                                            val approximateWord = words.getOrNull(words.size / 2) ?: "literature"
                                            quickSelectedWord = approximateWord.trim(',', '.', ';', ':', '!', '?', '"', '\'')
                                            viewModel.onTextSelected(quickSelectedWord ?: "")
                                        }
                                    )
                                }
                        )
                    }

                    // Touch navigation zones
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Left 25% tap: previous page
                        Box(
                            modifier = Modifier
                                .weight(0.25f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.prevPage()
                                }
                        )

                        // Center 50% tap: toggle toolbars
                        Box(
                            modifier = Modifier
                                .weight(0.50f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.toggleToolbars()
                                }
                        )

                        // Right 25% tap: next page
                        Box(
                            modifier = Modifier
                                .weight(0.25f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.nextPage()
                                }
                        )
                    }
                }
            } else {
                // Continuous scroll mode
                val fullChapterContent = currentChapter?.content ?: ""
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { viewModel.toggleToolbars() },
                                onLongPress = {
                                    quickSelectedWord = "fortune"
                                    viewModel.onTextSelected("fortune")
                                }
                            )
                        }
                ) {
                    if (currentChapter != null) {
                        item {
                            Text(
                                text = currentChapter.title,
                                fontFamily = prefs.font.fontFamily,
                                fontSize = (prefs.fontSizeSp + 4).sp,
                                fontWeight = FontWeight.Bold,
                                color = prefs.theme.accentColor,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }

                    item {
                        Text(
                            text = fullChapterContent,
                            fontFamily = prefs.font.fontFamily,
                            fontSize = prefs.fontSizeSp.sp,
                            lineHeight = (prefs.fontSizeSp * prefs.lineHeightMultiplier).sp,
                            color = prefs.theme.textColor,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Reserve space for bottom bar if visible
            Spacer(modifier = Modifier.height(if (uiState.isToolbarsVisible) 84.dp else 24.dp))
        }

        // Selection Action Bar Popup (When text / word is selected)
        if (quickSelectedWord != null) {
            ElevatedCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = prefs.theme.surfaceColor
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Selected: \"$quickSelectedWord\"",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = prefs.theme.textColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Highlight button
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFDE047).copy(alpha = 0.25f),
                            modifier = Modifier.clickable {
                                viewModel.addHighlight("#FDE047")
                                quickSelectedWord = null
                            }
                        ) {
                            Text(
                                text = "Highlight",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = prefs.theme.textColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        // Add note button
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = prefs.theme.accentColor.copy(alpha = 0.15f),
                            modifier = Modifier.clickable {
                                viewModel.openNoteDialog()
                                quickSelectedWord = null
                            }
                        ) {
                            Text(
                                text = "Add Note",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = prefs.theme.accentColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        // Define in Lexicon
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = prefs.theme.accentColor,
                            modifier = Modifier.clickable {
                                viewModel.lookupWord(quickSelectedWord ?: "")
                                quickSelectedWord = null
                            }
                        ) {
                            Text(
                                text = "Define",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        // Dismiss
                        TextButton(
                            onClick = {
                                quickSelectedWord = null
                                viewModel.clearTextSelection()
                            }
                        ) {
                            Text("✕", fontSize = 14.sp, color = prefs.theme.secondaryTextColor)
                        }
                    }
                }
            }
        }

        // Top Minimal Toolbar (Animated)
        AnimatedVisibility(
            visible = uiState.isToolbarsVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = prefs.theme.surfaceColor.copy(alpha = 0.95f),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Library",
                            tint = prefs.theme.textColor
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = uiState.book?.title ?: "ReadNest",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = prefs.theme.textColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = currentChapter?.title ?: "",
                            fontSize = 12.sp,
                            color = prefs.theme.secondaryTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Table of contents
                    IconButton(onClick = { viewModel.openToc() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.FormatListBulleted,
                            contentDescription = "Table of contents",
                            tint = prefs.theme.textColor
                        )
                    }

                    // Bookmark toggle
                    IconButton(onClick = { viewModel.toggleBookmark() }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                            tint = if (isBookmarked) Color(0xFFF59E0B) else prefs.theme.textColor
                        )
                    }

                    // Annotations & Notes
                    IconButton(onClick = { viewModel.openAnnotations() }) {
                        Icon(
                            imageVector = Icons.Outlined.StickyNote2,
                            contentDescription = "Highlights and Notes",
                            tint = prefs.theme.textColor
                        )
                    }

                    // Typography & Themes Settings
                    IconButton(onClick = { viewModel.openSettings() }) {
                        Icon(
                            imageVector = Icons.Outlined.FormatSize,
                            contentDescription = "Typography and Theme settings",
                            tint = prefs.theme.textColor
                        )
                    }
                }
            }
        }

        // Bottom Minimal Toolbar (Animated)
        AnimatedVisibility(
            visible = uiState.isToolbarsVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = prefs.theme.surfaceColor.copy(alpha = 0.95f),
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Page Progress Bar / Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.prevPage() },
                            enabled = currentPage > 0 || uiState.currentChapterIndex > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Previous page",
                                tint = prefs.theme.textColor
                            )
                        }

                        Slider(
                            value = currentPage.toFloat(),
                            onValueChange = { viewModel.jumpToPage(it.toInt()) },
                            valueRange = 0f..maxOf(1, pageCount - 1).toFloat(),
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = prefs.theme.accentColor,
                                activeTrackColor = prefs.theme.accentColor,
                                inactiveTrackColor = prefs.theme.secondaryTextColor.copy(alpha = 0.25f)
                            )
                        )

                        IconButton(
                            onClick = { viewModel.nextPage() },
                            enabled = currentPage < pageCount - 1 || uiState.currentChapterIndex < uiState.chapters.size - 1
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Next page",
                                tint = prefs.theme.textColor
                            )
                        }
                    }

                    // Progress info line
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Page ${currentPage + 1} of $pageCount",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = prefs.theme.secondaryTextColor
                        )

                        Text(
                            text = "${(uiState.book?.progressPercent ?: 0f).toInt()}% • ~${uiState.estimatedMinutesLeftInChapter} min left in chapter",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = prefs.theme.accentColor
                        )
                    }
                }
            }
        }

        // Screen Warmth Tint Overlay (Eye protection)
        if (prefs.screenWarmth > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFF9800).copy(alpha = prefs.screenWarmth))
            )
        }

        // Modals & Bottom Sheets
        if (uiState.isSettingsOpen) {
            ReaderSettingsSheet(
                preferences = prefs,
                onDismiss = { viewModel.closeSettings() },
                onThemeSelect = { viewModel.setTheme(it) },
                onFontSelect = { viewModel.setFont(it) },
                onAdjustFontSize = { viewModel.adjustFontSize(it) },
                onLineHeightSelect = { viewModel.setLineHeight(it) },
                onMarginSelect = { viewModel.setHorizontalMargin(it) },
                onLayoutModeSelect = { viewModel.setLayoutMode(it) },
                onWarmthChange = { viewModel.setScreenWarmth(it) }
            )
        }

        if (uiState.isTocOpen) {
            TableOfContentsSheet(
                chapters = uiState.chapters,
                currentChapterIndex = uiState.currentChapterIndex,
                preferences = prefs,
                onDismiss = { viewModel.closeToc() },
                onChapterSelect = { viewModel.jumpToChapter(it) }
            )
        }

        if (uiState.isAnnotationsOpen) {
            AnnotationsSheet(
                bookmarks = bookmarks,
                annotations = annotations,
                preferences = prefs,
                onDismiss = { viewModel.closeAnnotations() },
                onJumpToBookmark = { bm ->
                    viewModel.jumpToChapter(bm.chapterIndex, bm.pageIndex)
                },
                onJumpToAnnotation = { ann ->
                    viewModel.jumpToChapter(ann.chapterIndex, 0)
                },
                onDeleteBookmark = { viewModel.deleteBookmark(it) },
                onDeleteAnnotation = { viewModel.deleteAnnotation(it) }
            )
        }

        if (uiState.isDictionaryOpen && uiState.dictionaryEntry != null) {
            DictionarySheet(
                entry = uiState.dictionaryEntry!!,
                preferences = prefs,
                onDismiss = { viewModel.closeDictionary() }
            )
        }

        if (uiState.isAddNoteOpen) {
            AddNoteDialog(
                selectedText = uiState.selectedTextForAction ?: "",
                preferences = prefs,
                onDismiss = { viewModel.closeNoteDialog() },
                onSave = { note, color ->
                    viewModel.saveNote(note, color)
                }
            )
        }
    }
}
