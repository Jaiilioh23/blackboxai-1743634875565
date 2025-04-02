package com.example.mangareader.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {

    override suspend fun isMangaFavorite(mangaId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        val doc = firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(mangaId)
            .get()
            .await()
        return doc.exists()
    }

    override suspend fun setMangaFavorite(mangaId: String, isFavorite: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        val favoritesRef = firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(mangaId)

        if (isFavorite) {
            favoritesRef.set(mapOf("timestamp" to System.currentTimeMillis())).await()
        } else {
            favoritesRef.delete().await()
        }
    }

    override suspend fun getReadingProgress(mangaId: String): Int? {
        val userId = auth.currentUser?.uid ?: return null
        val doc = firestore.collection("users")
            .document(userId)
            .collection("progress")
            .document(mangaId)
            .get()
            .await()
        return doc.getLong("chapter")?.toInt()
    }

    override suspend fun setReadingProgress(mangaId: String, chapter: Int) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("progress")
            .document(mangaId)
            .set(mapOf(
                "chapter" to chapter,
                "timestamp" to System.currentTimeMillis()
            ))
            .await()
    }
}