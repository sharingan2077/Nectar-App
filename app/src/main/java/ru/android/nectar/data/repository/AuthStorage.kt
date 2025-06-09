package ru.android.nectar.data.repository

import android.content.Context
import androidx.core.content.edit
import ru.android.nectar.data.model.AuthData

object AuthStorage {
    private const val PREFS_NAME = "auth"
    private const val KEY_TOKEN = "jwt"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"

    fun saveAuthData(context: Context, token: String, userId: Int, username: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_TOKEN, token)
                putInt(KEY_USER_ID, userId)
                putString(KEY_USERNAME, username)
                apply()
            }
    }

    fun getAuthData(context: Context): AuthData? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val token = prefs.getString(KEY_TOKEN, null) ?: return null
        return AuthData(
            token = token,
            userId = prefs.getInt(KEY_USER_ID, -1),
            username = prefs.getString(KEY_USERNAME, "") ?: ""
        )
    }

    fun clearAuthData(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                clear()
                apply()
            }
    }
}