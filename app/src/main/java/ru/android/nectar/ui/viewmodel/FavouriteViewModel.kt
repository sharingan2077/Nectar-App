package ru.android.nectar.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _favouriteProducts = MutableStateFlow<List<ProductEntity>>(emptyList())  // StateFlow для списка продуктов
    val favouriteProducts: StateFlow<List<ProductEntity>> = _favouriteProducts

    fun isFavorite(productId: Int): Flow<Boolean> = repository.isFavorite(productId)

    fun getFavouriteProducts(): Flow<List<Int>> = repository.getFavouriteProductIds()

    fun getFavoriteProductEntities(): Flow<List<ProductEntity>> = flow {
        val productIds = repository.getFavouriteProductIds().first() // Получаем список избранных ID

        val products = productIds.mapNotNull { id -> repository.getProductById(id).firstOrNull() }
        emit(products)

    }.flowOn(Dispatchers.IO) // Запускаем на IO-потоке

    fun loadFavouriteProducts() {
        viewModelScope.launch {
            val productIds = repository.getFavouriteProductIds().first()
            val products = productIds.mapNotNull { id -> repository.getProductById(id).firstOrNull() }
            _favouriteProducts.value = products // Обновляем StateFlow
        }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            Log.d(TAG, "toggleFavourite, productId-$productId")
            val isFav = repository.isFavorite(productId).first()
            if (isFav) {
                Log.d(TAG, "toggleFavourite product isNotFav")
                repository.removeFavorite(productId)
            } else {
                Log.d(TAG, "toggleFavourite product isFav")
                repository.addFavorite(productId)
            }
            loadFavouriteProducts()
        }
    }
}
