package ru.android.nectar.ui.favourite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.android.nectar.data.local.entity.FavouriteEntity
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.repository.FavouriteRepository
import javax.inject.Inject

private const val TAG = "FavouriteViewModel"

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: FavouriteRepository) : ViewModel() {

    fun isFavorite(userId: Int, productId: Int): Flow<Boolean> = repository.isFavorite(userId, productId)

    fun getFavoriteProducts(userId: Int): Flow<List<Int>> = repository.getFavoriteProducts(userId)

    fun getFavoriteProductEntities(userId: Int): Flow<List<ProductEntity>> = flow {
        val productIds = repository.getFavoriteProducts(userId).first() // Получаем список избранных ID

        val products = productIds.mapNotNull { id -> repository.getProductById(id).firstOrNull() }
        emit(products)

    }.flowOn(Dispatchers.IO) // Запускаем на IO-потоке

    fun toggleFavorite(userId: Int, productId: Int) {
        viewModelScope.launch {
            Log.d(TAG, "toggleFavourite, userId-$userId, productId-$productId")
            val isFav = repository.isFavorite(userId, productId).first()
            if (isFav) {
                Log.d(TAG, "toggleFavourite product isNotFav")
                repository.removeFavorite(userId, productId)
            } else {
                Log.d(TAG, "toggleFavourite product isFav")
                repository.addFavorite(FavouriteEntity(userId, productId))
            }
        }
    }
}