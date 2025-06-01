# QuizzHub

QuizzHub is a smart Android quiz application that enables users to generate topic-based MCQs using Google's Gemini API. It is designed for fast, personalized revision with offline and online bookmarking, result tracking, and a clean user interface built with Kotlin and XML. The app also supports Firebase Authentication for secure login and data synchronization.


---

## Table of Contents
1. [Features](#features)
2. [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
3. [Contributing](#contributing)
4. [License](#license)

---

## Features
- **AI-Generated Quizzes**: QuizzHub uses the Gemini API to dynamically generate 10 multiple-choice questions based on a topic entered by the user. This allows users to instantly test their understanding of any subject without relying on pre-defined question sets.
- **Firebase Authentication**: The app supports secure user authentication using Firebase. Users can log in using either their Google account or an email and password combination. This enables personalized experiences and data sync across multiple devices.
- **Bookmarking (Offline and Online)**: Users can bookmark important or difficult questions for future revision. Bookmarks are saved locally using Room Database for offline access. When a user is logged in, bookmarks are automatically synced to Firebase Firestore, allowing cloud-based storage and retrieval across devices.
- **Performance Analysis**: After completing each quiz, users are provided with a detailed summary of their performance. This includes the total score, number of correct and incorrect answers, and a review of each question along with the user's selected and correct answers.
- **MVVM Architecture**: The app follows the Model-View-ViewModel (MVVM) architecture, which separates concerns for better code maintainability, testability, and scalability. This pattern ensures a clean structure between UI components, data management, and business logic.
---

## Getting Started
To get a local copy up and running, follow these steps.

### Prerequisites
- Android Studio or Visual Studio Code
- Firebase Project for API keys and configuration

### Installation

1. **Clone the repository**:
   ```
   git clone https://github.com/TERRA2k5/QuizzHub.git
   ```
2. **Open the project in Android Studio.**:

3. **Configure Firebase**:
- Download the google-services.json file from Firebase Console and place it in the android/app directory.
---

## Contributing

Contributions are welcome! If you'd like to improve Quizzhub, please fork the repository and create a pull request. For major changes, open an issue first to discuss what you’d like to change.
