package com.codealpha.fitnesstracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityAdapter(
    private val onEdit: (FitnessActivity) -> Unit,
    private val onDelete: (FitnessActivity) -> Unit
) : ListAdapter<FitnessActivity, ActivityAdapter.ActivityViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val activityInitial: TextView = itemView.findViewById(R.id.tvActivityInitial)
        private val activityType: TextView = itemView.findViewById(R.id.tvActivityType)
        private val activityDate: TextView = itemView.findViewById(R.id.tvActivityDate)
        private val duration: TextView = itemView.findViewById(R.id.tvDuration)
        private val calories: TextView = itemView.findViewById(R.id.tvCalories)
        private val notes: TextView = itemView.findViewById(R.id.tvNotes)
        private val editButton: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(activity: FitnessActivity) {
            activityInitial.text = activity.activityType.firstOrNull()
                ?.uppercaseChar()
                ?.toString()
                ?: "F"
            activityType.text = activity.activityType
            activityDate.text = formatDate(activity.date)
            duration.text = itemView.context.getString(
                R.string.duration_value,
                activity.durationMinutes
            )
            calories.text = itemView.context.getString(
                R.string.calories_value,
                activity.caloriesBurned
            )
            notes.text = activity.notes
            notes.isVisible = activity.notes.isNotBlank()

            itemView.setOnClickListener { onEdit(activity) }
            editButton.setOnClickListener { onEdit(activity) }
            deleteButton.setOnClickListener { onDelete(activity) }
        }

        private fun formatDate(isoDate: String): String {
            return try {
                val parser = SimpleDateFormat(DATABASE_DATE_PATTERN, Locale.US).apply {
                    isLenient = false
                }
                val formatter = SimpleDateFormat(DISPLAY_DATE_PATTERN, Locale.getDefault())
                parser.parse(isoDate)?.let { formatter.format(it) } ?: isoDate
            } catch (_: Exception) {
                isoDate
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<FitnessActivity>() {
        override fun areItemsTheSame(
            oldItem: FitnessActivity,
            newItem: FitnessActivity
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FitnessActivity,
            newItem: FitnessActivity
        ): Boolean = oldItem == newItem
    }

    companion object {
        private const val DATABASE_DATE_PATTERN = "yyyy-MM-dd"
        private const val DISPLAY_DATE_PATTERN = "dd MMM yyyy"
    }
}
