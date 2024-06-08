package com.example.quizzhub.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizzhub.model.SavedQuestion

@Dao
interface QuizDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quiz: SavedQuestion)

    @Delete
    suspend fun delete(quiz: SavedQuestion)

    @Query("SELECT * FROM saved ORDER BY id ASC")
    fun getAllBookmarks(): LiveData<List<SavedQuestion>>

}