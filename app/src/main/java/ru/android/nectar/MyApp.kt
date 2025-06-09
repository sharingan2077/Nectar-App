package ru.android.nectar

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import androidx.core.content.edit

private const val TAG = "MyApp"

@HiltAndroidApp
class MyApp : Application() {
    //Практика 3
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application created")

        val prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        darkTheme = prefs.getBoolean("dark_theme", false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        getSharedPreferences("theme_prefs", MODE_PRIVATE)
            .edit() {
                putBoolean("dark_theme", darkThemeEnabled)
            }
    }
    //Практика 3
}
