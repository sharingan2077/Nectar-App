package ru.android.nectar.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.android.nectar.data.model.AuthResponse
import ru.android.nectar.data.model.LoginRequest
import ru.android.nectar.data.model.RegisterRequest

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

}
