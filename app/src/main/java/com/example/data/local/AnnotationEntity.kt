package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "annotations")
data class AnnotationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Long,
    val chapterIndex: Int,
    val selectedText: String,
    val note: String = "",
    val colorHex: String = "#FDE047", // Yellow default
    val createdAt: Long = System.currentTimeMillis()
)
