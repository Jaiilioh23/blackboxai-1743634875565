package com.example.mangareader.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mangareader.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    private val splashDelay = 2000L // 2 seconds
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Main).launch {
            delay(splashDelay)
            navigateToAppropriateScreen()
        }
    }

    private fun navigateToAppropriateScreen() {
        val destination = if (auth.currentUser != null) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        
        startActivity(destination)
        finish()
    }
}