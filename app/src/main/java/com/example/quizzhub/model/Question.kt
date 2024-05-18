package com.example.quizzhub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val answers: List<String>,
    val correctAnswer: String,
    val question: String
):Parcelable