buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath ("com.android.tools.build:gradle:8.1.4") // Use a version that supports API 35
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}