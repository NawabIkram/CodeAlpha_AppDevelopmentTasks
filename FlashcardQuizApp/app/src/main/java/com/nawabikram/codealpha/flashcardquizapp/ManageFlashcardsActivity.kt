package com.nawabikram.codealpha.flashcardquizapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ManageFlashcardsActivity : AppCompatActivity() {

    private lateinit var storage: SharedPreferenceHelper
    private lateinit var adapter: FlashcardAdapter
    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_flashcards)

        storage = SharedPreferenceHelper(this)
        emptyTextView = findViewById(R.id.emptyFlashcardsTextView)
        recyclerView = findViewById(R.id.flashcardsRecyclerView)
        val addButton = findViewById<MaterialButton>(R.id.addFlashcardButton)

        adapter = FlashcardAdapter(
            flashcards = emptyList(),
            onEditClick = { flashcard ->
                val intent = Intent(this, AddEditFlashcardActivity::class.java)
                intent.putExtra(AddEditFlashcardActivity.EXTRA_FLASHCARD_ID, flashcard.id)
                startActivity(intent)
            },
            onDeleteClick = { flashcard ->
                storage.deleteFlashcard(flashcard.id)
                Toast.makeText(this, "Flashcard deleted", Toast.LENGTH_SHORT).show()
                loadFlashcards()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            startActivity(Intent(this, AddEditFlashcardActivity::class.java))
        }

        loadFlashcards()
    }

    override fun onResume() {
        super.onResume()
        loadFlashcards()
    }

    private fun loadFlashcards() {
        val flashcards = storage.getAllFlashcards()
        adapter.updateData(flashcards)

        if (flashcards.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}
