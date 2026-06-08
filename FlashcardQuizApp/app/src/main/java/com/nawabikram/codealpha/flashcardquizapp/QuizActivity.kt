package com.nawabikram.codealpha.flashcardquizapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class QuizActivity : AppCompatActivity() {

    private lateinit var storage: SharedPreferenceHelper
    private lateinit var questionTextView: TextView
    private lateinit var answerTextView: TextView
    private lateinit var counterTextView: TextView
    private lateinit var showAnswerButton: MaterialButton
    private lateinit var previousButton: MaterialButton
    private lateinit var nextButton: MaterialButton

    private var flashcards = mutableListOf<Flashcard>()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        storage = SharedPreferenceHelper(this)
        questionTextView = findViewById(R.id.questionTextView)
        answerTextView = findViewById(R.id.answerTextView)
        counterTextView = findViewById(R.id.counterTextView)
        showAnswerButton = findViewById(R.id.showAnswerButton)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)

        showAnswerButton.setOnClickListener {
            answerTextView.visibility = View.VISIBLE
        }

        previousButton.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showCurrentFlashcard()
            }
        }

        nextButton.setOnClickListener {
            if (currentIndex < flashcards.lastIndex) {
                currentIndex++
                showCurrentFlashcard()
            }
        }

        loadFlashcards()
    }

    override fun onResume() {
        super.onResume()
        loadFlashcards()
    }

    private fun loadFlashcards() {
        flashcards = storage.getAllFlashcards()
        if (currentIndex > flashcards.lastIndex) {
            currentIndex = 0
        }
        showCurrentFlashcard()
    }

    private fun showCurrentFlashcard() {
        if (flashcards.isEmpty()) {
            questionTextView.text = "No flashcards available. Add a flashcard first."
            answerTextView.text = ""
            counterTextView.text = "0 / 0"
            showAnswerButton.isEnabled = false
            previousButton.isEnabled = false
            nextButton.isEnabled = false
            return
        }

        val card = flashcards[currentIndex]
        questionTextView.text = card.question
        answerTextView.text = card.answer
        answerTextView.visibility = View.GONE
        counterTextView.text = "${currentIndex + 1} / ${flashcards.size}"

        showAnswerButton.isEnabled = true
        previousButton.isEnabled = currentIndex > 0
        nextButton.isEnabled = currentIndex < flashcards.lastIndex
    }
}
