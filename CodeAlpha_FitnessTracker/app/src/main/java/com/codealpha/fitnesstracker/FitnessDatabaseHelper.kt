package com.codealpha.fitnesstracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FitnessDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_ACTIVITIES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ACTIVITY_TYPE TEXT NOT NULL,
                $COLUMN_DURATION INTEGER NOT NULL,
                $COLUMN_CALORIES INTEGER NOT NULL,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_NOTES TEXT NOT NULL DEFAULT '',
                $COLUMN_CREATED_AT INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX index_activities_date ON $TABLE_ACTIVITIES($COLUMN_DATE)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 1) {
            onCreate(db)
        }
    }

    fun insertActivity(activity: FitnessActivity): Long {
        return writableDatabase.insert(TABLE_ACTIVITIES, null, activity.toContentValues())
    }

    fun updateActivity(activity: FitnessActivity): Int {
        return writableDatabase.update(
            TABLE_ACTIVITIES,
            activity.toContentValues(),
            "$COLUMN_ID = ?",
            arrayOf(activity.id.toString())
        )
    }

    fun deleteActivity(activityId: Long): Int {
        return writableDatabase.delete(
            TABLE_ACTIVITIES,
            "$COLUMN_ID = ?",
            arrayOf(activityId.toString())
        )
    }

    fun getAllActivities(): List<FitnessActivity> {
        val activities = mutableListOf<FitnessActivity>()
        val query = """
            SELECT $COLUMN_ID, $COLUMN_ACTIVITY_TYPE, $COLUMN_DURATION,
                   $COLUMN_CALORIES, $COLUMN_DATE, $COLUMN_NOTES
            FROM $TABLE_ACTIVITIES
            ORDER BY $COLUMN_DATE DESC, $COLUMN_ID DESC
        """.trimIndent()

        readableDatabase.rawQuery(query, null).use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID)
            val typeIndex = cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_TYPE)
            val durationIndex = cursor.getColumnIndexOrThrow(COLUMN_DURATION)
            val caloriesIndex = cursor.getColumnIndexOrThrow(COLUMN_CALORIES)
            val dateIndex = cursor.getColumnIndexOrThrow(COLUMN_DATE)
            val notesIndex = cursor.getColumnIndexOrThrow(COLUMN_NOTES)

            while (cursor.moveToNext()) {
                activities += FitnessActivity(
                    id = cursor.getLong(idIndex),
                    activityType = cursor.getString(typeIndex),
                    durationMinutes = cursor.getInt(durationIndex),
                    caloriesBurned = cursor.getInt(caloriesIndex),
                    date = cursor.getString(dateIndex),
                    notes = cursor.getString(notesIndex).orEmpty()
                )
            }
        }
        return activities
    }

    private fun FitnessActivity.toContentValues(): ContentValues {
        return ContentValues().apply {
            put(COLUMN_ACTIVITY_TYPE, activityType)
            put(COLUMN_DURATION, durationMinutes)
            put(COLUMN_CALORIES, caloriesBurned)
            put(COLUMN_DATE, date)
            put(COLUMN_NOTES, notes)
            put(COLUMN_CREATED_AT, System.currentTimeMillis())
        }
    }

    companion object {
        private const val DATABASE_NAME = "fitness_tracker.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_ACTIVITIES = "fitness_activities"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ACTIVITY_TYPE = "activity_type"
        private const val COLUMN_DURATION = "duration_minutes"
        private const val COLUMN_CALORIES = "calories_burned"
        private const val COLUMN_DATE = "activity_date"
        private const val COLUMN_NOTES = "notes"
        private const val COLUMN_CREATED_AT = "created_at"
    }
}
