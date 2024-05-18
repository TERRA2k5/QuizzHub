package com.example.quizzhub


data class Question(
    val answers: List<String>,
    val correctAnswer: String,
    val question: String
)