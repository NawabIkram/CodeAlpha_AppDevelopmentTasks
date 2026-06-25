package com.codealpha.fitnesstracker

data class FitnessSummary(
    val totalActivities: Int,
    val totalDurationMinutes: Int,
    val totalCalories: Int,
    val todayActivities: Int,
    val todayDurationMinutes: Int,
    val todayCalories: Int,
    val weeklyActivities: Int,
    val weeklyDurationMinutes: Int,
    val weeklyCalories: Int
)

/** Pure Kotlin summary logic, kept separate so it can be unit tested. */
object FitnessSummaryCalculator {
    fun calculate(
        activities: List<FitnessActivity>,
        todayIsoDate: String,
        weekStartIsoDate: String
    ): FitnessSummary {
        val todayItems = activities.filter { it.date == todayIsoDate }
        val weeklyItems = activities.filter { activity ->
            activity.date >= weekStartIsoDate && activity.date <= todayIsoDate
        }

        return FitnessSummary(
            totalActivities = activities.size,
            totalDurationMinutes = activities.sumOf { it.durationMinutes },
            totalCalories = activities.sumOf { it.caloriesBurned },
            todayActivities = todayItems.size,
            todayDurationMinutes = todayItems.sumOf { it.durationMinutes },
            todayCalories = todayItems.sumOf { it.caloriesBurned },
            weeklyActivities = weeklyItems.size,
            weeklyDurationMinutes = weeklyItems.sumOf { it.durationMinutes },
            weeklyCalories = weeklyItems.sumOf { it.caloriesBurned }
        )
    }
}
