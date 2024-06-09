package com.example.quizzhub.database

import android.util.Log
import android.widget.Toast
import com.example.quizzhub.model.SavedQuestion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId get() = auth.currentUser?.uid ?: throw IllegalStateException("No user logged in")

    suspend fun getBookmarks(): List<SavedQuestion> {
        return try {
            val snapshot = db.collection("users")
                .document(userId)
                .collection("bookmarks")
                .get()
                .await()

            val bookmarks = snapshot.documents.mapNotNull { document ->
                val bookmark = document.toObject(SavedQuestion::class.java)
                bookmark?.apply { firestoreId = document.id }
            }
            Log.d("FirestoreRepository", "Fetched ${bookmarks.size} bookmarks")
            bookmarks
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error fetching bookmarks", e)
            emptyList()
        }
    }

    suspend fun addBookmark(bookmark: SavedQuestion) {
        val documentReference = db.collection("users")
            .document(userId)
            .collection("bookmarks")
            .add(bookmark)
            .await()
        bookmark.firestoreId = documentReference.id
    }

    suspend fun deleteBookmark(firestoreId: String) {
        db.collection("users")
            .document(userId)
            .collection("bookmarks")
            .document(firestoreId)
            .delete()
            .await()
    }
}
