package com.example.data.model

data class Chapter(
    val title: String,
    val content: String,
    val wordCount: Int = content.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
) {
    val estimatedMinutes: Int
        get() = maxOf(1, (wordCount / 220))
}
