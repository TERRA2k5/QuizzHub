package com.example.quizzhub.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(tableName = "quiz")
@Parcelize
data class Question(
    val options: List<String>,
    val correctAnswer: String,
    val question: String
):Parcelable