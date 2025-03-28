import com.android.build.gradle.internal.tasks.databinding.DataBindingGenBaseClassesTask
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
//    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.0.21"
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "ru.android.nectar"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.android.nectar"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    // Allow references to generated code
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    val kotlin_version = "2.0.0"
    implementation("org.jetbrains.kotlin:kotlin-metadata-jvm:$kotlin_version")

//    runtimeOnly("org.jetbrains.kotlin:kotlinx-metadata-jvm:0.9.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-auth")
//    implementation("com.google.android.gms:play-services-auth")
//    implementation("com.google.android.gms:play-services-auth-api-phone")

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Navigation
    val navigationVersion = "2.8.7"
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    implementation(libs.kotlinx.serialization.json)

    //Lifecycle
    val lifecycleVersion = "2.8.7"
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //Room
    val roomVersion = "2.6.1"
    implementation(libs.androidx.room.runtime)
//    ksp(libs.androidx.room.compiler)
    kapt("androidx.room:room-compiler:2.6.1")  // Для Kotlin
    implementation(libs.androidx.room.ktx)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation("androidx.fragment:fragment-ktx:1.8.6") // или более новая версия

    implementation("com.google.android.material:material:1.12.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}