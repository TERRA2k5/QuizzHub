package com.example.quizzhub.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizzhub.model.Quiz

@Dao
interface QuizDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuestion(quiz: Quiz)

    @Delete
    suspend fun deleteQuestion(quiz: Quiz)

    @Query("SELECT * FROM quiz")
    suspend fun getQuestion(): LiveData<List<Quiz>>
}