package ru.android.nectar.data.repository

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface OpenFoodApiService {
    @GET("cgi/search.pl?json=1")
    fun searchProducts(@Query("search_terms") query: String): Call<FoodSearchResponse>
}