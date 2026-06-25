# CodeAlpha Fitness Tracker

**CodeAlpha Internship Task 3 — Fitness Tracker App**

A complete native Android fitness-tracking application built with Kotlin and XML. Users can manually log daily activities, review totals, monitor today's progress, and see a current-week summary. All data is stored locally on the device using SQLite.

## GitHub Repository Name

`CodeAlpha_FitnessTracker`

## Features

- Add a daily fitness activity manually
- Store activity type, duration, calories, date, and optional notes
- Edit existing activity records
- Delete activities with confirmation
- RecyclerView activity history
- Dashboard with:
  - Total activities
  - Total workout duration
  - Total calories burned
  - Today's activities, minutes, and calories
- Current-week summary with activities, minutes, calories, and progress bar
- Empty state for a new user
- Input validation for all required fields
- Modern gradient background, rounded cards, chips, icons, and dialog inputs
- Automatic dashboard refresh after add, edit, or delete
- Offline-only operation with no internet permission

## Technologies Used

- Kotlin
- Android XML layouts
- AndroidX AppCompat
- ConstraintLayout
- RecyclerView and ListAdapter/DiffUtil
- Material Components
- SQLiteOpenHelper
- Gradle

## Project Configuration

- Package: `com.codealpha.fitnesstracker`
- Minimum SDK: 23
- Target SDK: 35
- Compile SDK: 35
- Android Gradle Plugin: 8.7.3
- Gradle: 8.9
- Kotlin: 1.9.24
- Java/JVM target: 17

## How to Run in Android Studio

1. Extract the ZIP file.
2. Open Android Studio.
3. Select **Open**.
4. Choose the extracted `CodeAlpha_FitnessTracker` folder.
5. Allow Gradle sync to finish.
6. Make sure Android SDK 35 is installed.
7. Select an emulator or connect an Android device running Android 6.0 or newer.
8. Click **Run**.

## Storage

The app creates a local SQLite database named `fitness_tracker.db`. No account, network request, cloud database, or internet permission is used.

## Main Source Files

- `MainActivity.kt` — dashboard, dialogs, validation, summaries, and CRUD actions
- `FitnessActivity.kt` — activity data model
- `FitnessDatabaseHelper.kt` — SQLite database operations
- `ActivityAdapter.kt` — RecyclerView list adapter
- `FitnessSummaryCalculator.kt` — testable daily/weekly dashboard calculations
- `activity_main.xml` — dashboard and list screen
- `dialog_add_edit_activity.xml` — add/edit form
- `item_activity.xml` — activity list row

## Validation

See `VALIDATION_REPORT.md` for the checks completed before packaging. Unit tests are included under `app/src/test`.

## Submission Note

Push the extracted project to a GitHub repository named `CodeAlpha_FitnessTracker` for the CodeAlpha internship submission.
