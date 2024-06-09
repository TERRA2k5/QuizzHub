package com.example.quizzhub.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude


@Entity(tableName = "saved")
data class SavedQuestion(
    @PrimaryKey(autoGenerate = true) var id: Int? = 0,
    val correctAnswer: String,
    val question: String,
    val explain: String,
    @get:Exclude @set:Exclude var firestoreId: String? = null
){
    // Default no-argument constructor required by Firestore
    constructor() : this(0 , " " , " " , " " , " ")
}
