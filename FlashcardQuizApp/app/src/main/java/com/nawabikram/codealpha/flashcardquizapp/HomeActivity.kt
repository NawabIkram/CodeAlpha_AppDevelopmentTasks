package com.nawabikram.codealpha.flashcardquizapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val startQuizButton = findViewById<MaterialButton>(R.id.startQuizButton)
        val manageFlashcardsButton = findViewById<MaterialButton>(R.id.manageFlashcardsButton)

        startQuizButton.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        manageFlashcardsButton.setOnClickListener {
            startActivity(Intent(this, ManageFlashcardsActivity::class.java))
        }
    }
}
