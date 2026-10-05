#!/bin/bash
set -e

echo "Setting up Android project for GitHub Actions..."

# Create gradlew if not exists
if [ ! -f "android/gradlew" ]; then
    echo "Creating Gradle wrapper..."
    cd android
    gradle wrapper --gradle-version 8.5
    cd ..
fi

# Make gradlew executable
chmod +x android/gradlew

echo "Setup complete! You can now push to GitHub to trigger the build workflow."
echo ""
echo "To build the APK:"
echo "  1. Make a commit and push to main branch"
echo "  2. Go to: https://github.com/uday03041989-cmd/sap-comprehensive-troubleshooting-suite/actions"
echo "  3. Click on the 'Build Android APK' workflow"
echo "  4. Wait for the build to complete"
echo "  5. Download the APK from the Artifacts section"
