#!/bin/bash

adb shell settings put global policy_control immersive.full=*
adb shell monkey -p com.snt.phoney -v 50000
adb shell settings put global policy_control null