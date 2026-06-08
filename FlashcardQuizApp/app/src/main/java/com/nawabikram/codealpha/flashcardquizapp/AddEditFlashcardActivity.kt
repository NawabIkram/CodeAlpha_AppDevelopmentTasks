package com.nawabikram.codealpha.flashcardquizapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddEditFlashcardActivity : AppCompatActivity() {

    private lateinit var storage: SharedPreferenceHelper
    private lateinit var screenTitleTextView: TextView
    private lateinit var questionEditText: TextInputEditText
    private lateinit var answerEditText: TextInputEditText
    private var editingFlashcardId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_flashcard)

        storage = SharedPreferenceHelper(this)
        screenTitleTextView = findViewById(R.id.addEditTitleTextView)
        questionEditText = findViewById(R.id.questionEditText)
        answerEditText = findViewById(R.id.answerEditText)
        val saveButton = findViewById<MaterialButton>(R.id.saveFlashcardButton)

        editingFlashcardId = intent.getIntExtra(EXTRA_FLASHCARD_ID, -1)
        loadFlashcardForEditing()

        saveButton.setOnClickListener {
            saveFlashcard()
        }
    }

    private fun loadFlashcardForEditing() {
        if (editingFlashcardId == -1) {
            screenTitleTextView.text = "Add Flashcard"
            return
        }

        val flashcard = storage.getFlashcardById(editingFlashcardId)
        if (flashcard == null) {
            Toast.makeText(this, "Flashcard not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        screenTitleTextView.text = "Edit Flashcard"
        questionEditText.setText(flashcard.question)
        answerEditText.setText(flashcard.answer)
    }

    private fun saveFlashcard() {
        val question = questionEditText.text.toString().trim()
        val answer = answerEditText.text.toString().trim()

        questionEditText.error = null
        answerEditText.error = null

        if (question.isEmpty()) {
            questionEditText.error = "Question is required"
            questionEditText.requestFocus()
            return
        }

        if (answer.isEmpty()) {
            answerEditText.error = "Answer is required"
            answerEditText.requestFocus()
            return
        }

        if (editingFlashcardId == -1) {
            storage.addFlashcard(question, answer)
            Toast.makeText(this, "Flashcard added", Toast.LENGTH_SHORT).show()
        } else {
            storage.updateFlashcard(editingFlashcardId, question, answer)
            Toast.makeText(this, "Flashcard updated", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    companion object {
        const val EXTRA_FLASHCARD_ID = "extra_flashcard_id"
    }
}
