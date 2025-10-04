# Demo1 Android App

An Android application built with Gradle and Firebase integration.

## Project Setup

### Prerequisites
- Android Studio
- JDK 17
- Android SDK 34
- Minimum Android API Level 24 (Android 7.0)

### Dependencies
- AndroidX AppCompat
- Material Design Components
- Glide 4.16.0
- Firebase Database
- Firebase Storage
- JExcel API 2.6.12

## Building the Project

To build the project, use either Android Studio or run the following command:

```sh
./gradlew build
```

For Windows:
```sh
gradlew.bat build
```

## Testing

The project includes both unit tests and instrumented tests:

- Unit Tests: Located in the `app/src/test` directory
- Instrumented Tests: Located in `app/src/androidTest` directory

To run the tests:
```sh
./gradlew test          # Unit tests
./gradlew connectedTest # Instrumented tests
```

## Project Structure

```
app/
├── src/
│   ├── main/
│   ├── test/
│   └── androidTest/
├── build.gradle
└── proguard-rules.pro
```

## Build Configuration

- `compileSdk`: 34
- `minSdk`: 24
- `targetSdk`: 34
- `versionCode`: 1
- `versionName`: "1.0"

## License

This project uses ProGuard for code optimization and obfuscation. See [proguard-rules.pro](app/proguard-rules.pro) for configuration details.
