package com.example.quizzhub.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(tableName = "quiz")
@Parcelize
data class Quiz(
    val quiz: List<Question>
): Parcelable