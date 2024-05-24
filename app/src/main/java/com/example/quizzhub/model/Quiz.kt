package com.example.quizzhub.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize


@Parcelize
data class Quiz(
    val quiz: List<Question>
): Parcelable