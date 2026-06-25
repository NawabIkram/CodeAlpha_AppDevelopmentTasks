package com.codealpha.fitnesstracker

import org.junit.Assert.assertEquals
import org.junit.Test

class FitnessSummaryCalculatorTest {

    @Test
    fun calculate_returnsCorrectTotalsForTodayAndWeek() {
        val activities = listOf(
            FitnessActivity(1, "Running", 30, 250, "2026-06-25"),
            FitnessActivity(2, "Walking", 20, 100, "2026-06-25"),
            FitnessActivity(3, "Yoga", 40, 130, "2026-06-23"),
            FitnessActivity(4, "Cycling", 60, 500, "2026-06-15")
        )

        val summary = FitnessSummaryCalculator.calculate(
            activities = activities,
            todayIsoDate = "2026-06-25",
            weekStartIsoDate = "2026-06-22"
        )

        assertEquals(4, summary.totalActivities)
        assertEquals(150, summary.totalDurationMinutes)
        assertEquals(980, summary.totalCalories)
        assertEquals(2, summary.todayActivities)
        assertEquals(50, summary.todayDurationMinutes)
        assertEquals(350, summary.todayCalories)
        assertEquals(3, summary.weeklyActivities)
        assertEquals(90, summary.weeklyDurationMinutes)
        assertEquals(480, summary.weeklyCalories)
    }

    @Test
    fun calculate_handlesEmptyList() {
        val summary = FitnessSummaryCalculator.calculate(
            activities = emptyList(),
            todayIsoDate = "2026-06-25",
            weekStartIsoDate = "2026-06-22"
        )

        assertEquals(0, summary.totalActivities)
        assertEquals(0, summary.todayActivities)
        assertEquals(0, summary.weeklyActivities)
    }
}
