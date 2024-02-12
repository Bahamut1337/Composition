// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.0.0-Beta2" apply false
    kotlin("kapt") version "1.9.22"
    id("androidx.navigation.safeargs") version "2.7.7" apply false
}