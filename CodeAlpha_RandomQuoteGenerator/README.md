# CodeAlpha Random Quote Generator

**Internship Task:** CodeAlpha Android App Development — Task 2: Random Quote Generator

A complete native Android application that displays inspiring random quotes with a modern, responsive XML-based user interface.

## Features

- Shows a random quote when the app opens
- Includes 36 built-in quotes
- Generates a different quote on every button click
- Prevents the same quote from appearing twice continuously
- Smooth fade-out and fade-in animation when changing quotes
- Clearly displays quote text and author name
- Copy quote to the Android clipboard
- Share quote through the native Android share sheet
- Add or remove favorite quotes
- Saves favorites locally with `SharedPreferences`
- Favorites dialog with saved quote list, individual remove action, and clear-all action
- Modern gradient background, rounded cards, rounded buttons, and clean spacing
- Responsive `ScrollView` layout for different Android screen sizes
- Fully offline and requires no internet permission

## Technologies Used

- Kotlin
- Native Android SDK
- XML layouts and XML drawable resources
- ViewBinding
- SharedPreferences
- Android Clipboard API
- Android Share Intent
- Gradle

## Project Information

- **Project name:** `CodeAlpha_RandomQuoteGenerator`
- **Package name:** `com.codealpha.randomquotegenerator`
- **Minimum SDK:** 23 (Android 6.0)
- **Target SDK:** 34
- **UI:** XML layouts only
- **Jetpack Compose:** Not used
- **Internet permission:** Not required

## How to Run in Android Studio

1. Extract the ZIP file.
2. Open Android Studio.
3. Select **Open**.
4. Choose the extracted `CodeAlpha_RandomQuoteGenerator` folder.
5. Allow Gradle sync to finish.
6. Select an emulator or connected Android device running Android 6.0 or newer.
7. Click **Run**.

## Build from Command Line

On macOS/Linux:

```bash
./gradlew assembleDebug
```

On Windows:

```bat
gradlew.bat assembleDebug
```

The debug APK will be generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Suggested GitHub Repository Name

```text
CodeAlpha_RandomQuoteGenerator
```
