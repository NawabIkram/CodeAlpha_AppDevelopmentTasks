package com.codealpha.randomquotegenerator

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.codealpha.randomquotegenerator.databinding.ActivityMainBinding
import com.codealpha.randomquotegenerator.databinding.DialogFavoritesBinding
import com.codealpha.randomquotegenerator.databinding.ItemFavoriteQuoteBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private val preferences by lazy {
        getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private var currentQuote: Quote? = null

    private val quotes: List<Quote> = listOf(
        Quote(1, "The secret of getting ahead is getting started.", "Mark Twain"),
        Quote(2, "Success is the sum of small efforts, repeated day in and day out.", "Robert Collier"),
        Quote(3, "It always seems impossible until it is done.", "Nelson Mandela"),
        Quote(4, "Believe you can and you are halfway there.", "Theodore Roosevelt"),
        Quote(5, "What you do today can improve all your tomorrows.", "Ralph Marston"),
        Quote(6, "Dream big and dare to fail.", "Norman Vaughan"),
        Quote(7, "Act as if what you do makes a difference. It does.", "William James"),
        Quote(8, "Quality is not an act, it is a habit.", "Aristotle"),
        Quote(9, "Well done is better than well said.", "Benjamin Franklin"),
        Quote(10, "The future depends on what you do today.", "Mahatma Gandhi"),
        Quote(11, "Do what you can, with what you have, where you are.", "Theodore Roosevelt"),
        Quote(12, "You miss one hundred percent of the shots you do not take.", "Wayne Gretzky"),
        Quote(13, "A goal without a plan is just a wish.", "Antoine de Saint-Exupéry"),
        Quote(14, "The best way out is always through.", "Robert Frost"),
        Quote(15, "Turn your wounds into wisdom.", "Oprah Winfrey"),
        Quote(16, "Nothing will work unless you do.", "Maya Angelou"),
        Quote(17, "Great things are done by a series of small things brought together.", "Vincent van Gogh"),
        Quote(18, "The only limit to our realization of tomorrow is our doubts of today.", "Franklin D. Roosevelt"),
        Quote(19, "Start where you are. Use what you have. Do what you can.", "Arthur Ashe"),
        Quote(20, "Opportunities do not happen. You create them.", "Chris Grosser"),
        Quote(21, "Do not wait. The time will never be just right.", "Napoleon Hill"),
        Quote(22, "If you can dream it, you can do it.", "Walt Disney"),
        Quote(23, "Success usually comes to those who are too busy to be looking for it.", "Henry David Thoreau"),
        Quote(24, "The harder I work, the luckier I get.", "Samuel Goldwyn"),
        Quote(25, "Make each day your masterpiece.", "John Wooden"),
        Quote(26, "The journey of a thousand miles begins with one step.", "Lao Tzu"),
        Quote(27, "Either you run the day or the day runs you.", "Jim Rohn"),
        Quote(28, "You are never too old to set another goal or to dream a new dream.", "C. S. Lewis"),
        Quote(29, "Small deeds done are better than great deeds planned.", "Peter Marshall"),
        Quote(30, "The best preparation for tomorrow is doing your best today.", "H. Jackson Brown Jr."),
        Quote(31, "Courage is resistance to fear, mastery of fear, not absence of fear.", "Mark Twain"),
        Quote(32, "Keep your face always toward the sunshine, and shadows will fall behind you.", "Walt Whitman"),
        Quote(33, "Energy and persistence conquer all things.", "Benjamin Franklin"),
        Quote(34, "The only way to do great work is to love what you do.", "Steve Jobs"),
        Quote(35, "It is never too late to be what you might have been.", "George Eliot"),
        Quote(36, "A little progress each day adds up to big results.", "Satya Nani")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureSystemBars()
        setClickListeners()
        showRandomQuote(animate = false)
    }

    private fun configureSystemBars() {
        window.statusBarColor = getColor(R.color.gradient_start)
        window.navigationBarColor = getColor(R.color.surface)
    }

    private fun setClickListeners() = with(binding) {
        btnNewQuote.setOnClickListener { showRandomQuote(animate = true) }
        btnCopy.setOnClickListener { copyCurrentQuote() }
        btnShare.setOnClickListener { shareCurrentQuote() }
        btnFavorite.setOnClickListener { toggleFavorite() }
        btnViewFavorites.setOnClickListener { showFavoritesDialog() }
    }

    private fun showRandomQuote(animate: Boolean) {
        val availableQuotes = quotes.filter { it.id != currentQuote?.id }
        val nextQuote = availableQuotes.randomOrNull() ?: quotes.first()

        if (!animate) {
            updateQuote(nextQuote)
            return
        }

        binding.quoteContentGroup.animate().cancel()
        binding.quoteContentGroup.animate()
            .alpha(0f)
            .setDuration(FADE_OUT_DURATION)
            .withEndAction {
                updateQuote(nextQuote)
                binding.quoteContentGroup.animate()
                    .alpha(1f)
                    .setDuration(FADE_IN_DURATION)
                    .start()
            }
            .start()
    }

    private fun updateQuote(quote: Quote) {
        currentQuote = quote
        binding.tvQuote.text = getString(R.string.quote_format, quote.text)
        binding.tvAuthor.text = getString(R.string.author_format, quote.author)
        updateFavoriteButton()
    }

    private fun copyCurrentQuote() {
        val quote = currentQuote ?: return
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(getString(R.string.clipboard_label), formatQuote(quote))
        )
        Toast.makeText(this, R.string.quote_copied, Toast.LENGTH_SHORT).show()
    }

    private fun shareCurrentQuote() {
        val quote = currentQuote ?: return
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            putExtra(Intent.EXTRA_TEXT, formatQuote(quote))
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
    }

    private fun toggleFavorite() {
        val quote = currentQuote ?: return
        val favoriteIds = getFavoriteIds()
        val isNowFavorite = if (favoriteIds.contains(quote.id.toString())) {
            favoriteIds.remove(quote.id.toString())
            false
        } else {
            favoriteIds.add(quote.id.toString())
            true
        }

        saveFavoriteIds(favoriteIds)
        updateFavoriteButton()

        val message = if (isNowFavorite) {
            R.string.added_to_favorites
        } else {
            R.string.removed_from_favorites
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateFavoriteButton() {
        val quote = currentQuote ?: return
        val isFavorite = getFavoriteIds().contains(quote.id.toString())

        binding.btnFavorite.apply {
            text = getString(
                if (isFavorite) R.string.favorited_button else R.string.favorite_button
            )
            setBackgroundResource(
                if (isFavorite) R.drawable.bg_button_favorite_active
                else R.drawable.bg_button_secondary
            )
            setTextColor(
                getColor(if (isFavorite) R.color.white else R.color.primary_dark)
            )
            contentDescription = getString(
                if (isFavorite) R.string.remove_favorite_description
                else R.string.add_favorite_description
            )
        }
    }

    private fun showFavoritesDialog() {
        val dialogBinding = DialogFavoritesBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        renderFavorites(dialogBinding)

        dialogBinding.btnClearFavorites.setOnClickListener {
            if (getFavoriteIds().isEmpty()) {
                Toast.makeText(this, R.string.no_favorites_yet, Toast.LENGTH_SHORT).show()
            } else {
                saveFavoriteIds(mutableSetOf())
                updateFavoriteButton()
                renderFavorites(dialogBinding)
                Toast.makeText(this, R.string.favorites_cleared, Toast.LENGTH_SHORT).show()
            }
        }

        dialogBinding.btnCloseFavorites.setOnClickListener { dialog.dismiss() }

        dialog.setOnShowListener {
            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
        }
        dialog.show()
    }

    private fun renderFavorites(dialogBinding: DialogFavoritesBinding) {
        val favoriteIds = getFavoriteIds()
        val favoriteQuotes = quotes.filter { favoriteIds.contains(it.id.toString()) }

        dialogBinding.favoritesContainer.removeAllViews()
        dialogBinding.tvEmptyFavorites.visibility =
            if (favoriteQuotes.isEmpty()) View.VISIBLE else View.GONE
        dialogBinding.btnClearFavorites.isEnabled = favoriteQuotes.isNotEmpty()
        dialogBinding.btnClearFavorites.alpha = if (favoriteQuotes.isNotEmpty()) 1f else 0.5f

        favoriteQuotes.forEach { quote ->
            val itemBinding = ItemFavoriteQuoteBinding.inflate(
                LayoutInflater.from(this),
                dialogBinding.favoritesContainer,
                false
            )
            itemBinding.tvFavoriteQuote.text = getString(R.string.quote_format, quote.text)
            itemBinding.tvFavoriteAuthor.text = getString(R.string.author_format, quote.author)
            itemBinding.btnRemoveFavorite.setOnClickListener {
                val updatedIds = getFavoriteIds().apply { remove(quote.id.toString()) }
                saveFavoriteIds(updatedIds)
                updateFavoriteButton()
                renderFavorites(dialogBinding)
            }
            dialogBinding.favoritesContainer.addView(itemBinding.root)
        }
    }

    private fun getFavoriteIds(): MutableSet<String> {
        return preferences.getStringSet(KEY_FAVORITE_IDS, emptySet())
            ?.toMutableSet()
            ?: mutableSetOf()
    }

    private fun saveFavoriteIds(ids: Set<String>) {
        preferences.edit().putStringSet(KEY_FAVORITE_IDS, ids.toSet()).apply()
    }

    private fun formatQuote(quote: Quote): String {
        return getString(R.string.share_quote_format, quote.text, quote.author)
    }

    companion object {
        private const val PREFERENCES_NAME = "quote_generator_preferences"
        private const val KEY_FAVORITE_IDS = "favorite_quote_ids"
        private const val FADE_OUT_DURATION = 160L
        private const val FADE_IN_DURATION = 280L
    }
}
