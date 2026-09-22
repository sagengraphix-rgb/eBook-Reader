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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReaderPreferences

@Composable
fun AddNoteDialog(
    selectedText: String,
    preferences: ReaderPreferences,
    onDismiss: () -> Unit,
    onSave: (note: String, colorHex: String) -> Unit
) {
    var noteText by remember { mutableStateOf("") }
    val colors = listOf(
        "#FDE047" to Color(0xFFFDE047), // Yellow
        "#86EFAC" to Color(0xFF86EFAC), // Green
        "#FDA4AF" to Color(0xFFFDA4AF), // Coral
        "#93C5FD" to Color(0xFF93C5FD)  // Blue
    )
    var selectedColor by remember { mutableStateOf(colors[0].first) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Note & Highlight",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = preferences.theme.textColor
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "\"$selectedText\"",
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    color = preferences.theme.secondaryTextColor,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(preferences.theme.backgroundColor)
                        .padding(10.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Highlight Color",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = preferences.theme.secondaryTextColor
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    colors.forEach { (hex, col) ->
                        val isSelected = selectedColor == hex
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(col)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) preferences.theme.textColor else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = { Text("Write your thoughts or reflections...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(noteText, selectedColor) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = preferences.theme.accentColor
                )
            ) {
                Text("Save Note")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = preferences.theme.secondaryTextColor)
            }
        },
        containerColor = preferences.theme.surfaceColor
    )
}
