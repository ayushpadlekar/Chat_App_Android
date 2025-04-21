# Chat App 📱

This is a simple Firebase-based chat application built using **Jetpack Compose**, designed for the Android Internship Assignment at **Achilies TwoFour Labs**.

### ⬇️ Download APK
https://github.com/ayushpadlekar/Chat_App_Android/releases/download/v1.0.0/Chat_App_Android.apk

---

## 🛠️ Setup Instructions

1. Clone the repo:
   ```bash
   git clone https://github.com/ayushpadlekar/Chat_App_Android.git

2. Open the project in Android Studio.

3. Add your own Firebase configuration:
    * Go to Firebase Console
    * Create a project
    * Add an Android app (package name must match this project)
    * Download google-services.json and place it in: app/google-services.json
    * Generate SHA-1 & SHA-256 from android studio & paste them in Firebase Console's Project Settings.

4. Enable the following in Firebase:
    - Firebase Authentication (Email/Password)
    - Firestore Database
  
5. Create Firestore Index (required):
      - If you see an index error with a link in Logcat, click the link and create the composite index from Firebase Console.
      - or directly navigate to Indexes tab in firestore and click 'Add Index'.
      - Enter these values -
         - Collection ID - chats
         - Fields indexed - users ~Arrays, timestamp ~Descending, __name __ ~Descending
         - Query scope - Collection

6. Build and run the project on an emulator or real device.

---

## ✅ Features Completed

- 🔐 Firebase Authentication (Login / Signup in single screen using toggle)
  - Built-in input validation (email, password, username)
  - Error messages shown directly inside fields
  - Toast messages on login/signup success/failure
- 💬 Realtime One-on-One Chat
  - Create new chats with users
  - Send and receive messages in real-time
  - Timestamp shown for each message
- 📋 Chat List Screen
  - Shows all recent conversations
  - Displays last message and time
  - Tapping opens the full chat
- 🔍 Search User Screen
  - Search users by email or username
  - Start chat by selecting a user
- 🧭 Jetpack Compose Navigation
- 💅 Material 3 UI with custom theme
  - Light custom theme with primary, secondary, and tertiary colors
  - Consistent top bars and buttons

---

## 🚀 Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- Firebase Authentication
- Firebase Firestore
- MVVM Architecture
- StateFlow for reactive state management

