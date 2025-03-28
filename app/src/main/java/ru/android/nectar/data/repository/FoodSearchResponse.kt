package ru.android.nectar.data.repository

data class FoodSearchResponse(
    val products: List<FoodProduct>
)

data class FoodProduct(
    val id: String?,
    val product_name: String?,
    val image_url: String?
)