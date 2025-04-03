// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
//    id("com.google.devtools.ksp") version "2.1.10-1.0.30" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

}
buildscript {
//    repositories {
//        google()
//        mavenCentral()
//    }
    dependencies {
        val nav_version = "2.8.8"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
//        val kotlin_version = "1.9.0"
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}