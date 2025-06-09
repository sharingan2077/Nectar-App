package ru.android.nectar.data.model

import kotlinx.serialization.Serializable

data class RegisterRequest(val login: String, val password: String, val username: String)
data class LoginRequest(val login: String, val password: String)

//data class TokenResponse(val token: String)

// В shared-модуле или отдельно для Ktor
@Serializable
data class AuthResponse(
    val token: String,
    val userId: Int,
    val username: String
)

// В Android-модуле
data class AuthData(
    val token: String,
    val userId: Int,
    val username: String
)