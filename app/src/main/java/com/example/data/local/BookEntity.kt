package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val author: String,
    val coverColorHex: String,
    val coverImageUri: String? = null,
    val format: String, // "EPUB", "TXT", "PDF"
    val totalChapters: Int = 1,
    val currentChapterIndex: Int = 0,
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val progressPercent: Float = 0f,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val dateAdded: Long = System.currentTimeMillis(),
    val tags: String = "General",
    val contentRaw: String = "",
    val chaptersJson: String = "",
    val isFinished: Boolean = false,
    val estimatedMinutes: Int = 30
)
