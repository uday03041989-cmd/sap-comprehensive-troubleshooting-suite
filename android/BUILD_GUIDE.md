# SAP BASIS Troubleshooter - Android App Build Guide

## Automated APK Build in GitHub Actions

This project is configured to automatically build the Android APK in GitHub using GitHub Actions workflows.

### Workflows Included

1. **build-apk.yml** - Builds Debug and Release APKs on every push to main/develop
2. **release-apk.yml** - Creates a GitHub Release with APK when you tag a version
3. **lint-check.yml** - Runs Android lint checks on pull requests

### How to Build APK in GitHub

#### Method 1: Automatic Build on Push

1. Make changes to your Android code
2. Commit and push to main branch:
   ```bash
   git add .
   git commit -m "Update Android app"
   git push origin main
   ```

3. Go to GitHub Actions:
   - https://github.com/uday03041989-cmd/sap-comprehensive-troubleshooting-suite/actions

4. Click on the **"Build Android APK"** workflow

5. Wait for the build to complete (usually 5-10 minutes)

6. Download APK from Artifacts:
   - **sap-basis-troubleshooter-debug** - Debug APK for testing
   - **sap-basis-troubleshooter-release** - Release APK for distribution

#### Method 2: Create a Release with Tag

1. Create a git tag and push:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. Go to GitHub Actions

3. The **"Build and Release Android APK"** workflow will:
   - Build the Release APK
   - Create a GitHub Release
   - Upload APK to the release

4. Download APK from GitHub Releases:
   - https://github.com/uday03041989-cmd/sap-comprehensive-troubleshooting-suite/releases

#### Method 3: Manual Trigger

1. Go to GitHub Actions
2. Click on **"Build Android APK"** workflow
3. Click **"Run workflow"** button
4. Wait for completion and download artifacts

### APK Files

**Debug APK** (`app-debug.apk`)
- For testing on emulator or device
- No signing required
- Easier to debug
- Larger file size

**Release APK** (`app-release.apk`)
- For production distribution
- Must be signed with a keystore
- Optimized and minified
- Ready for Google Play Store

### Install APK on Device

#### Option 1: Using ADB (Android Debug Bridge)

```bash
# Connect your Android device via USB
# Enable USB debugging on device

adb install app-debug.apk
```

#### Option 2: Direct Installation

1. Download APK from GitHub Actions Artifacts
2. Transfer to your Android device
3. Open file manager on device
4. Tap the APK file
5. Tap "Install"

#### Option 3: Email or Cloud

1. Download APK from GitHub
2. Email to device
3. Or upload to Google Drive and download from device

### Project Structure

```
android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/sapbasis/troubleshooter/
│   │       │   ├── MainActivity.kt
│   │       │   └── DiagnosticKnowledge.kt
│   │       ├── res/
│   │       │   ├── values/
│   │       │   ├── drawable/
│   │       │   └── layout/
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
```

### Features Included

- ✅ SAP ABAP troubleshooting
- ✅ Java app diagnostics
- ✅ BOBJ report issues
- ✅ BODS ETL troubleshooting
- ✅ SAP Router connectivity
- ✅ SAP Cloud Connector / BTP
- ✅ SSL certificate checks
- ✅ HANA database diagnostics
- ✅ ASE database troubleshooting
- ✅ Material Design 3 UI
- ✅ Jetpack Compose
- ✅ Rule-based diagnosis engine

### Troubleshooting Build Issues

#### Build Fails with "SDK not found"

- GitHub Actions automatically sets up Android SDK 34
- This should not be an issue in GitHub Actions
- If building locally, ensure Android SDK is installed

#### Build Takes Too Long

- First build may take 10-15 minutes
- Gradle cache is enabled for faster subsequent builds
- Subsequent builds typically take 5-10 minutes

#### APK Upload Fails

- Ensure `app/build/outputs/apk/` directory exists
- Check workflow logs for detailed error messages
- Verify Gradle build completed successfully

### Next Steps

1. **Test the APK**
   - Install on Android device or emulator
   - Test all troubleshooting categories
   - Verify UI and interactions

2. **Customize Diagnostics**
   - Edit `DiagnosticKnowledge.kt` to add more rules
   - Customize UI in `MainActivity.kt`
   - Add icons and branding in `res/drawable/`

3. **Add Features**
   - Implement SQLite database for saved cases
   - Add API integration with backend service
   - Create searchable knowledge base
   - Add export/share incident reports

4. **Release to Google Play**
   - Sign the release APK with your keystore
   - Configure Play Store account
   - Upload APK and app metadata
   - Submit for review

### CI/CD Status

View build status, logs, and artifacts:
- Actions Dashboard: https://github.com/uday03041989-cmd/sap-comprehensive-troubleshooting-suite/actions
- Releases: https://github.com/uday03041989-cmd/sap-comprehensive-troubleshooting-suite/releases

### Support

For issues or questions:
1. Check GitHub Actions workflow logs
2. Review Android Lint reports
3. Verify Android SDK and Gradle versions
4. Consult Android documentation: https://developer.android.com
