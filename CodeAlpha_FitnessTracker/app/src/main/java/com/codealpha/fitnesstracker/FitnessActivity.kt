package com.codealpha.fitnesstracker

/** Represents one manually logged fitness activity. */
data class FitnessActivity(
    val id: Long = 0L,
    val activityType: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val date: String,
    val notes: String = ""
)
