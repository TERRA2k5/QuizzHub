package com.example.quizzhub.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved")
data class SavedQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val correctAnswer: String,
    val question: String,
    val explain: String
)
