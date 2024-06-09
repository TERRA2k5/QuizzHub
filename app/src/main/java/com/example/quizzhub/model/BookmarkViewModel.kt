package com.example.quizzhub.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizzhub.database.BookmarkRepository
import com.example.quizzhub.database.FirestoreRepository
import com.example.quizzhub.database.QuizDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(private val application: Application, private val repository: BookmarkRepository) : AndroidViewModel(application) {
//    private val repository: BookmarkRepository
    val allBookmarks: LiveData<List<SavedQuestion>> = repository.allBookmarks
    private val firestoreRepository: FirestoreRepository

    init {
        firestoreRepository = FirestoreRepository()
        syncWithFirestore()
    }

    fun syncWithFirestore() = viewModelScope.launch {
        try {
            val firestoreBookmarks = firestoreRepository.getBookmarks()
            firestoreBookmarks.forEach { bookmark ->
                repository.insert(bookmark)
                Log.d("BookmarkViewModel", bookmark.firestoreId.toString())
            }
            Log.d("BookmarkViewModel", "Success syncing with Firestore")
        } catch (e: Exception) {
            Log.e("BookmarkViewModel", "Error syncing with Firestore", e)
        }
    }

    fun insert(bookmark: SavedQuestion) = viewModelScope.launch {
        try {
            repository.insert(bookmark)
            firestoreRepository.addBookmark(bookmark)
        } catch (e: Exception) {
            Log.e("BookmarkViewModel", "Error inserting bookmark", e)
        }
    }

    fun delete(bookmark: SavedQuestion) = viewModelScope.launch {
        try {
            repository.delete(bookmark)
            bookmark.firestoreId?.let { firestoreRepository.deleteBookmark(it) }
        } catch (e: Exception) {
            Log.e("BookmarkViewModel", "Error deleting bookmark", e)
        }
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

