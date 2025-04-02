package com.example.mangareader

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MangaReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        Firebase.initialize(this)
        
        // Initialize other app-wide components
        initializeAppComponents()
    }

    private fun initializeAppComponents() {
        // Will be used for dependency injection setup
        // and other app-wide initializations
    }
}