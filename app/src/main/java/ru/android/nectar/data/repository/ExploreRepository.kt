package ru.android.nectar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.android.nectar.data.local.dao.ExploreDao
import ru.android.nectar.data.local.entity.ProductEntity

class ExploreRepository(private val exploreDao: ExploreDao) {

    fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return if (query.isBlank()) {
            flowOf(emptyList()) // Возвращаем пустой список, если запрос пустой
        } else {
            exploreDao.searchProducts(query)
        }
    }

//    private val api = Retrofit.Builder()
//        .baseUrl("https://world.openfoodfacts.org/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(OpenFoodApiService::class.java)
//
//    fun searchProducts(query: String, type: String): Flow<List<FoodProduct>> = flow {
//        if (query.isBlank()) {
//            emit(emptyList())
//        } else {
//            val response = api.searchProducts(query).execute()
//            if (response.isSuccessful) {
//                val products = response.body()?.products?.map { foodProduct ->
//                    FoodProduct(
//                        id = foodProduct.id ?: "",
//                        product_name = foodProduct.product_name ?: "Без названия",
//                        image_url = foodProduct.image_url ?: ""
//                    )
//                } ?: emptyList()
//                emit(products)
//            } else {
//                emit(emptyList())
//            }
//        }
//    }

}
