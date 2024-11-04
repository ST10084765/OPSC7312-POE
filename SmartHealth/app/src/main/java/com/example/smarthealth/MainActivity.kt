package com.example.smarthealth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_TIMEOUT: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            // Start the LoginActivity after the delay
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close the MainActivity so that it can't be returned to
        }, SPLASH_SCREEN_TIMEOUT)
    }
}
