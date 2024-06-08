package com.example.quizzhub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizzhub.model.SavedQuestion

@Database(entities = [SavedQuestion::class], version = 1 , exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quesDAO(): QuizDAO

    // this object is template ... used everywhere
    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        QuizDatabase::class.java,
                        "bookmarkDB").build()
                }
            }
            return INSTANCE!!

        }
    }
}