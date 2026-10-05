# SAP BASIS Troubleshooter Android APK

This Android project is designed to provide a mobile troubleshooting experience for:
- SAP ABAP
- Java
- BOBJ
- BODS
- SAP Router
- SAP Cloud Connector / BTP connectivity
- SSL certificate issues
- SAP HANA database
- SAP ASE database

## Prerequisites

Before building the APK, install:
- JDK 17
- Android Studio
- Android SDK 34
- Android SDK Build Tools

## Build the APK

Open a terminal in the `android` directory and run:

```bash
./gradlew assembleDebug
```

For a release build:

```bash
./gradlew assembleRelease
```

The APK will be generated in:

```bash
android/app/build/outputs/apk/debug/app-debug.apk
```

or:

```bash
android/app/build/outputs/apk/release/app-release.apk
```

## Android SDK path

If Gradle cannot find the SDK, add your local SDK path to:

```bash
android/local.properties
```

Example:

```properties
sdk.dir=/Users/yourname/Library/Android/sdk
```

## App features

- Category-based SAP troubleshooting guides
- Rule-based diagnosis for common SAP BASIS problems
- Quick recommendations and checklists
- Mobile-friendly interface for on-site support teams
- Extensible knowledge base for future modules

## Notes

This app currently provides a production-ready foundation and guided diagnostic logic for SAP BASIS support operations. It can be expanded with live SAP connectivity checks, log parsing, and incident reporting when needed.
