package com.nawabikram.codealpha.flashcardquizapp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class SharedPreferenceHelper(context: Context) {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getAllFlashcards(): MutableList<Flashcard> {
        ensureStarterFlashcards()

        val cards = mutableListOf<Flashcard>()
        val jsonText = preferences.getString(KEY_FLASHCARDS, "[]") ?: "[]"
        val jsonArray = JSONArray(jsonText)

        for (index in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(index)
            cards.add(
                Flashcard(
                    id = item.getInt("id"),
                    question = item.getString("question"),
                    answer = item.getString("answer")
                )
            )
        }

        return cards
    }

    fun getFlashcardById(id: Int): Flashcard? {
        return getAllFlashcards().firstOrNull { it.id == id }
    }

    fun addFlashcard(question: String, answer: String) {
        val cards = getAllFlashcards()
        val nextId = (cards.maxOfOrNull { it.id } ?: 0) + 1
        cards.add(Flashcard(nextId, question, answer))
        saveFlashcards(cards)
    }

    fun updateFlashcard(id: Int, question: String, answer: String) {
        val updatedCards = getAllFlashcards().map { card ->
            if (card.id == id) {
                card.copy(question = question, answer = answer)
            } else {
                card
            }
        }
        saveFlashcards(updatedCards)
    }

    fun deleteFlashcard(id: Int) {
        val updatedCards = getAllFlashcards().filter { it.id != id }
        saveFlashcards(updatedCards)
    }

    private fun ensureStarterFlashcards() {
        if (preferences.contains(KEY_FLASHCARDS)) return

        val starterCards = listOf(
            Flashcard(1, "What is Kotlin?", "Kotlin is a modern programming language used for Android development."),
            Flashcard(2, "What is Android Studio?", "Android Studio is the official IDE for Android app development."),
            Flashcard(3, "What does XML define in Android?", "XML is commonly used to define Android screen layouts.")
        )
        saveFlashcards(starterCards)
    }

    private fun saveFlashcards(cards: List<Flashcard>) {
        val jsonArray = JSONArray()

        cards.forEach { card ->
            val item = JSONObject()
            item.put("id", card.id)
            item.put("question", card.question)
            item.put("answer", card.answer)
            jsonArray.put(item)
        }

        preferences.edit()
            .putString(KEY_FLASHCARDS, jsonArray.toString())
            .apply()
    }

    companion object {
        private const val PREF_NAME = "flashcard_preferences"
        private const val KEY_FLASHCARDS = "flashcards"
    }
}
