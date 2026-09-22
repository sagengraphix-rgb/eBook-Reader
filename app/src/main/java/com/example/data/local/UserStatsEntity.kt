package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey
    val id: Int = 1,
    val booksRead: Int = 0,
    val totalPagesRead: Int = 0,
    val totalMinutesRead: Int = 0,
    val currentStreakDays: Int = 1,
    val lastReadingDate: String = ""
)
