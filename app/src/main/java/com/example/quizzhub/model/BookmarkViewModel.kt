package com.example.quizzhub.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizzhub.database.BookmarkRepository
import com.example.quizzhub.database.QuizDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(private val application: Application, private val repository: BookmarkRepository) : AndroidViewModel(application) {
//    private val repository: BookmarkRepository
    val allBookmarks: LiveData<List<SavedQuestion>> = repository.allBookmarks

    init {
//        val bookmarkDao = QuizDatabase.getDatabase(application)
//        repository = BookmarkRepository(bookmarkDao)
    }

    fun insert(bookmark: SavedQuestion) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(bookmark)
    }

    fun delete(bookmark: SavedQuestion) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(bookmark)
    }

}


class BookmarkViewModelFactory(private val application: Application , private val repository: BookmarkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookmarkViewModel(application , repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

