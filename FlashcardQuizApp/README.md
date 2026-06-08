# CodeAlpha_FlashcardQuizApp

CodeAlpha App Development Internship Task 1.

## Project Overview

CodeAlpha_FlashcardQuizApp is a simple native Android flashcard quiz app for studying. Users can view questions, reveal answers, move between flashcards, and manage their own flashcards.

## Features

- Splash Screen
- Home Screen with Start Quiz and Manage Flashcards buttons
- Quiz Screen that shows the question first
- Show Answer button to reveal the answer
- Previous and Next buttons for flashcard navigation
- Manage Flashcards screen with a list of saved cards
- Add new flashcards
- Edit existing flashcards
- Delete flashcards
- Local data saving using SharedPreferences
- Validation for empty question and answer fields
- Toast messages for add, update, and delete actions

## Tech Stack

- Native Android Development
- Kotlin
- XML layouts
- AndroidX AppCompat
- Material Components
- RecyclerView
- ConstraintLayout
- SharedPreferences with JSON storage

## Folder Structure

```text
FlashcardQuizApp/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/nawabikram/codealpha/flashcardquizapp/
│       │   ├── AddEditFlashcardActivity.kt
│       │   ├── Flashcard.kt
│       │   ├── FlashcardAdapter.kt
│       │   ├── HomeActivity.kt
│       │   ├── ManageFlashcardsActivity.kt
│       │   ├── QuizActivity.kt
│       │   ├── SharedPreferenceHelper.kt
│       │   └── SplashActivity.kt
│       └── res/
│           ├── drawable/
│           ├── layout/
│           └── values/
├── build.gradle
├── settings.gradle
└── README.md
```

## Screens

1. Splash Screen
2. Home Screen
3. Quiz Screen
4. Manage Flashcards Screen
5. Add/Edit Flashcard Screen

## How To Run In Android Studio

1. Open Android Studio.
2. Select **Open**.
3. Choose the folder:
   `CodeAlpha_AppDevelopmentTasks/FlashcardQuizApp`
4. Wait for Gradle sync to finish.
5. Connect an Android device or start an emulator.
6. Click **Run**.

## Local Storage

The app saves flashcards locally using SharedPreferences. Flashcards are stored as JSON text, so the data remains available after the app is closed and reopened.

## GitHub Repository

This task is stored inside:

```text
CodeAlpha_AppDevelopmentTasks/FlashcardQuizApp
```

Repository:

```text
NawabIkram/CodeAlpha_AppDevelopmentTasks
```
