package com.nawabikram.codealpha.flashcardquizapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class FlashcardAdapter(
    private var flashcards: List<Flashcard>,
    private val onEditClick: (Flashcard) -> Unit,
    private val onDeleteClick: (Flashcard) -> Unit
) : RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flashcard, parent, false)
        return FlashcardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        holder.bind(flashcards[position])
    }

    override fun getItemCount(): Int = flashcards.size

    fun updateData(newFlashcards: List<Flashcard>) {
        flashcards = newFlashcards
        notifyDataSetChanged()
    }

    inner class FlashcardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.itemQuestionTextView)
        private val answerTextView: TextView = itemView.findViewById(R.id.itemAnswerTextView)
        private val editButton: MaterialButton = itemView.findViewById(R.id.itemEditButton)
        private val deleteButton: MaterialButton = itemView.findViewById(R.id.itemDeleteButton)

        fun bind(flashcard: Flashcard) {
            questionTextView.text = flashcard.question
            answerTextView.text = flashcard.answer

            editButton.setOnClickListener {
                onEditClick(flashcard)
            }

            deleteButton.setOnClickListener {
                onDeleteClick(flashcard)
            }
        }
    }
}
