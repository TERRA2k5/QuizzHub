package com.example.quizzhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
    }

    override fun onPause() {
        super.onPause()

        finish()
    }
}