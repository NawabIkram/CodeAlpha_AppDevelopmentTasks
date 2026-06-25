package com.codealpha.fitnesstracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.Executors
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var rootView: View
    private lateinit var databaseHelper: FitnessDatabaseHelper
    private lateinit var activityAdapter: ActivityAdapter

    private lateinit var tvTotalActivities: TextView
    private lateinit var tvTotalDuration: TextView
    private lateinit var tvTotalCalories: TextView
    private lateinit var tvTodayActivities: TextView
    private lateinit var tvTodayDetails: TextView
    private lateinit var tvWeekRange: TextView
    private lateinit var tvWeeklyActivities: TextView
    private lateinit var tvWeeklyDuration: TextView
    private lateinit var tvWeeklyCalories: TextView
    private lateinit var tvWeeklyProgress: TextView
    private lateinit var weeklyProgressBar: ProgressBar
    private lateinit var emptyState: View
    private lateinit var recyclerView: RecyclerView

    private val databaseExecutor = Executors.newSingleThreadExecutor()
    private val databaseDateFormat = SimpleDateFormat(DATABASE_DATE_PATTERN, Locale.US).apply {
        isLenient = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = FitnessDatabaseHelper(applicationContext)
        bindViews()
        setupRecyclerView()

        findViewById<MaterialButton>(R.id.btnAddActivity).setOnClickListener {
            showActivityDialog(null)
        }

        loadActivities()
    }

    private fun bindViews() {
        rootView = findViewById(R.id.rootLayout)
        tvTotalActivities = findViewById(R.id.tvTotalActivities)
        tvTotalDuration = findViewById(R.id.tvTotalDuration)
        tvTotalCalories = findViewById(R.id.tvTotalCalories)
        tvTodayActivities = findViewById(R.id.tvTodayActivities)
        tvTodayDetails = findViewById(R.id.tvTodayDetails)
        tvWeekRange = findViewById(R.id.tvWeekRange)
        tvWeeklyActivities = findViewById(R.id.tvWeeklyActivities)
        tvWeeklyDuration = findViewById(R.id.tvWeeklyDuration)
        tvWeeklyCalories = findViewById(R.id.tvWeeklyCalories)
        tvWeeklyProgress = findViewById(R.id.tvWeeklyProgress)
        weeklyProgressBar = findViewById(R.id.weeklyProgressBar)
        emptyState = findViewById(R.id.emptyState)
        recyclerView = findViewById(R.id.recyclerActivities)
    }

    private fun setupRecyclerView() {
        activityAdapter = ActivityAdapter(
            onEdit = { activity -> showActivityDialog(activity) },
            onDelete = { activity -> confirmDelete(activity) }
        )
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = activityAdapter
            setHasFixedSize(false)
        }
    }

    private fun loadActivities() {
        databaseExecutor.execute {
            val activities = databaseHelper.getAllActivities()
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                activityAdapter.submitList(activities)
                emptyState.isVisible = activities.isEmpty()
                recyclerView.isVisible = activities.isNotEmpty()
                updateDashboard(activities)
            }
        }
    }

    private fun updateDashboard(activities: List<FitnessActivity>) {
        val today = databaseDateFormat.format(Calendar.getInstance().time)
        val weekStartCalendar = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val weekStart = databaseDateFormat.format(weekStartCalendar.time)
        val summary = FitnessSummaryCalculator.calculate(activities, today, weekStart)

        tvTotalActivities.text = summary.totalActivities.toString()
        tvTotalDuration.text = getString(
            R.string.minutes_short_value,
            summary.totalDurationMinutes
        )
        tvTotalCalories.text = getString(
            R.string.kcal_short_value,
            summary.totalCalories
        )
        tvTodayActivities.text = summary.todayActivities.toString()
        tvTodayDetails.text = getString(
            R.string.today_details,
            summary.todayDurationMinutes,
            summary.todayCalories
        )

        tvWeekRange.text = getString(
            R.string.week_range,
            formatDisplayDate(weekStart),
            formatDisplayDate(today)
        )
        tvWeeklyActivities.text = summary.weeklyActivities.toString()
        tvWeeklyDuration.text = getString(
            R.string.minutes_short_value,
            summary.weeklyDurationMinutes
        )
        tvWeeklyCalories.text = getString(
            R.string.kcal_short_value,
            summary.weeklyCalories
        )
        tvWeeklyProgress.text = getString(
            R.string.weekly_progress_value,
            summary.weeklyDurationMinutes,
            WEEKLY_MINUTES_GOAL
        )
        weeklyProgressBar.progress = min(
            summary.weeklyDurationMinutes,
            WEEKLY_MINUTES_GOAL
        )
    }

    private fun showActivityDialog(existingActivity: FitnessActivity?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_activity, null)

        val typeLayout = dialogView.findViewById<TextInputLayout>(R.id.layoutActivityType)
        val durationLayout = dialogView.findViewById<TextInputLayout>(R.id.layoutDuration)
        val caloriesLayout = dialogView.findViewById<TextInputLayout>(R.id.layoutCalories)
        val dateLayout = dialogView.findViewById<TextInputLayout>(R.id.layoutDate)
        val activityTypeInput = dialogView.findViewById<AutoCompleteTextView>(R.id.inputActivityType)
        val durationInput = dialogView.findViewById<EditText>(R.id.inputDuration)
        val caloriesInput = dialogView.findViewById<EditText>(R.id.inputCalories)
        val dateInput = dialogView.findViewById<EditText>(R.id.inputDate)
        val notesInput = dialogView.findViewById<EditText>(R.id.inputNotes)

        val activityTypes = resources.getStringArray(R.array.activity_types)
        activityTypeInput.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, activityTypes)
        )

        var selectedDate = existingActivity?.date ?: databaseDateFormat.format(
            Calendar.getInstance().time
        )
        dateInput.setText(formatDisplayDate(selectedDate))

        existingActivity?.let { activity ->
            activityTypeInput.setText(activity.activityType, false)
            durationInput.setText(activity.durationMinutes.toString())
            caloriesInput.setText(activity.caloriesBurned.toString())
            notesInput.setText(activity.notes)
        }

        val openDatePicker = View.OnClickListener {
            val calendar = calendarFromIsoDate(selectedDate)
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDate = String.format(
                        Locale.US,
                        "%04d-%02d-%02d",
                        year,
                        month + 1,
                        dayOfMonth
                    )
                    dateInput.setText(formatDisplayDate(selectedDate))
                    dateLayout.error = null
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        dateInput.setOnClickListener(openDatePicker)
        dateLayout.setEndIconOnClickListener(openDatePicker)

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(
                if (existingActivity == null) {
                    R.string.add_activity_title
                } else {
                    R.string.edit_activity_title
                }
            )
            .setView(dialogView)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(
                if (existingActivity == null) R.string.save_activity else R.string.update_activity,
                null
            )
            .create()

        dialog.setOnShowListener {
            dialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener {
                    typeLayout.error = null
                    durationLayout.error = null
                    caloriesLayout.error = null
                    dateLayout.error = null

                    val activityType = activityTypeInput.text?.toString()?.trim().orEmpty()
                    val duration = durationInput.text?.toString()?.trim()?.toIntOrNull()
                    val calories = caloriesInput.text?.toString()?.trim()?.toIntOrNull()
                    val notes = notesInput.text?.toString()?.trim().orEmpty()

                    var isValid = true
                    if (activityType.isBlank()) {
                        typeLayout.error = getString(R.string.error_activity_type)
                        isValid = false
                    }
                    if (duration == null || duration <= 0) {
                        durationLayout.error = getString(R.string.error_positive_duration)
                        isValid = false
                    }
                    if (calories == null || calories <= 0) {
                        caloriesLayout.error = getString(R.string.error_positive_calories)
                        isValid = false
                    }
                    if (selectedDate.isBlank()) {
                        dateLayout.error = getString(R.string.error_date_required)
                        isValid = false
                    }

                    if (!isValid || duration == null || calories == null) {
                        return@setOnClickListener
                    }

                    val activity = FitnessActivity(
                        id = existingActivity?.id ?: 0L,
                        activityType = activityType,
                        durationMinutes = duration,
                        caloriesBurned = calories,
                        date = selectedDate,
                        notes = notes
                    )

                    val positiveButton = dialog.getButton(
                        android.content.DialogInterface.BUTTON_POSITIVE
                    )
                    positiveButton.isEnabled = false

                    databaseExecutor.execute {
                        val success = if (existingActivity == null) {
                            databaseHelper.insertActivity(activity) != -1L
                        } else {
                            databaseHelper.updateActivity(activity) > 0
                        }

                        runOnUiThread {
                            if (isFinishing || isDestroyed) return@runOnUiThread
                            if (success) {
                                dialog.dismiss()
                                Snackbar.make(
                                    rootView,
                                    if (existingActivity == null) {
                                        R.string.activity_added
                                    } else {
                                        R.string.activity_updated
                                    },
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                loadActivities()
                            } else {
                                positiveButton.isEnabled = true
                                Snackbar.make(
                                    rootView,
                                    R.string.database_error,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
        }
        dialog.show()
    }

    private fun confirmDelete(activity: FitnessActivity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_activity_title)
            .setMessage(getString(R.string.delete_activity_message, activity.activityType))
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                databaseExecutor.execute {
                    val deleted = databaseHelper.deleteActivity(activity.id) > 0
                    runOnUiThread {
                        if (isFinishing || isDestroyed) return@runOnUiThread
                        Snackbar.make(
                            rootView,
                            if (deleted) R.string.activity_deleted else R.string.database_error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        if (deleted) loadActivities()
                    }
                }
            }
            .show()
    }

    private fun calendarFromIsoDate(isoDate: String): Calendar {
        val calendar = Calendar.getInstance()
        try {
            databaseDateFormat.parse(isoDate)?.let { calendar.time = it }
        } catch (_: Exception) {
            // Keep today's date when parsing fails.
        }
        return calendar
    }

    private fun formatDisplayDate(isoDate: String): String {
        return try {
            val date = databaseDateFormat.parse(isoDate) ?: return isoDate
            SimpleDateFormat(DISPLAY_DATE_PATTERN, Locale.getDefault()).format(date)
        } catch (_: Exception) {
            isoDate
        }
    }

    override fun onDestroy() {
        databaseExecutor.shutdown()
        super.onDestroy()
    }

    companion object {
        private const val DATABASE_DATE_PATTERN = "yyyy-MM-dd"
        private const val DISPLAY_DATE_PATTERN = "dd MMM yyyy"
        private const val WEEKLY_MINUTES_GOAL = 150
    }
}
