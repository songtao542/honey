./gradlew :app:assembleDebug --stacktrace
adb uninstall com.snt.phoney
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start com.snt.phoney/com.snt.phoney.ui.signup.SignupActivity