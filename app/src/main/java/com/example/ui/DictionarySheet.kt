package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DictionaryEntry
import com.example.data.model.ReaderPreferences

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DictionarySheet(
    entry: DictionaryEntry,
    preferences: ReaderPreferences,
    onDismiss: () -> Unit
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
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = entry.word,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = preferences.theme.textColor
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${entry.phonetic}  •  ${entry.partOfSpeech}",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = preferences.theme.secondaryTextColor
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = preferences.theme.accentColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoStories,
                            contentDescription = null,
                            tint = preferences.theme.accentColor,
                            modifier = Modifier.height(14.dp)
                        )
                        Text(
                            text = " In-App Lexicon",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = preferences.theme.accentColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = preferences.theme.backgroundColor
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Definition",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = preferences.theme.accentColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = entry.definition,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = preferences.theme.textColor
                    )

                    if (!entry.example.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Example in context:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = preferences.theme.secondaryTextColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${entry.example}\"",
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 20.sp,
                            color = preferences.theme.textColor.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            if (entry.synonyms.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Synonyms",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = preferences.theme.secondaryTextColor,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    entry.synonyms.forEach { syn ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(syn, fontSize = 12.sp) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = preferences.theme.backgroundColor,
                                labelColor = preferences.theme.textColor
                            )
                        )
                    }
                }
            }
        }
    }
}
