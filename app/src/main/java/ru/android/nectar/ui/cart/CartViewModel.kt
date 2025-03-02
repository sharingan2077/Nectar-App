package ru.android.nectar.ui.cart

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
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.repository.CartRepository
import javax.inject.Inject

private const val TAG = "CartViewModel"

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: CartRepository) : ViewModel() {

    private val _cartProducts = MutableStateFlow<List<ProductEntity>>(emptyList())  // StateFlow для списка продуктов
    val cartProducts: StateFlow<List<ProductEntity>> = _cartProducts

    fun isCart(userId: Int, productId: Int): Flow<Boolean> = repository.isCart(userId, productId)

    fun getCartProducts(userId: Int): Flow<List<Int>> = repository.getCartProducts(userId)

    fun getCartProduct(userId : Int, productId: Int) : Flow<CartEntity> = repository.getCartProduct(userId, productId)

    fun getCartProductEntities(userId: Int): Flow<List<ProductEntity>> = flow {
        val productIds = repository.getCartProducts(userId).first() // Получаем список избранных ID

//        val products = productIds.mapNotNull { id -> repository.getProductById(id).firstOrNull() }
        val products = productIds.mapNotNull { id -> repository.getProductById(id).firstOrNull() }
        _cartProducts.value = products // Обновляем StateFlow

        emit(products)

    }.flowOn(Dispatchers.IO) // Запускаем на IO-потоке


    fun loadCartProducts(userId: Int) {
        viewModelScope.launch {
            val productIds = repository.getCartProducts(userId).first()
            val products = productIds.mapNotNull { id -> repository.getProductById(id).firstOrNull() }
            _cartProducts.value = products // Обновляем StateFlow
        }
    }

    fun addCart(userId: Int, productId: Int) {
        viewModelScope.launch {
            Log.d(TAG, "addCart, userId-$userId, productId-$productId")
            val isCart = repository.isCart(userId, productId).first()
            if (isCart) {
                Log.d(TAG, "toggleCart product isNotCart")
                repository.removeCart(userId, productId)
            } else {
                Log.d(TAG, "toggleCart product isCart")
                repository.addCart(CartEntity(userId, productId, 1))
            }
        }
    }

    fun incrementCount(userId: Int, productId: Int) {
        viewModelScope.launch {
            repository.incrementCount(userId, productId)
        }
    }

    fun decrementCount(userId: Int, productId: Int) {
        viewModelScope.launch {
            repository.decrementCount(userId, productId)
        }
    }

    fun removeCart(userId: Int, productId: Int) {
         viewModelScope.launch {
             repository.removeCart(userId, productId)
             loadCartProducts(userId)
         }
    }
}