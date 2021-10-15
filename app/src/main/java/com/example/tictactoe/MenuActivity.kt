package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun goToGame(view: android.view.View) {
        val intent = Intent(this, MainActivity::class.java).apply {  }
        startActivity(intent)
    }

    fun goToCredits(view: android.view.View) {
        val intent = Intent(this, CreditsActivity::class.java).apply {  }
        startActivity(intent)
    }
}