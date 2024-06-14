package com.example.quizzhub.database

import androidx.lifecycle.LiveData
import com.example.quizzhub.model.SavedQuestion

class BookmarkRepository(private val bookmarkDao: QuizDatabase) {
    val allBookmarks: LiveData<List<SavedQuestion>> = bookmarkDao.quesDAO().getAllBookmarks()

    suspend fun insert(bookmark: SavedQuestion) {
        bookmarkDao.quesDAO().insert(bookmark)
    }

    suspend fun delete(bookmark: SavedQuestion) {
        bookmarkDao.quesDAO().delete(bookmark)
    }

    suspend fun deleteAll() {
        bookmarkDao.quesDAO().deleteAll()
    }

    suspend fun getAllBookmarksList(): List<SavedQuestion> {
        return bookmarkDao.quesDAO().getAllBookmarksList()
    }

}
