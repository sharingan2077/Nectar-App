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
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.repository.CartRepository
import javax.inject.Inject

private const val TAG = "CartViewModel"

data class CartProduct(
    val product: ProductEntity,
    val count: Int
)

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: CartRepository) : ViewModel() {

    private val _cartProducts = MutableStateFlow<List<CartProduct>>(emptyList())
    val cartProducts: StateFlow<List<CartProduct>> = _cartProducts

    fun isCart(productId: Int): Flow<Boolean> = repository.isCart(productId)

//    fun getCartProducts(): Flow<List<Int>> = repository.getCartProducts()

    fun getCartProduct(productId: Int): Flow<CartEntity?> = repository.getCartProduct(productId)

//    fun getCartProductEntities(): Flow<List<ProductEntity>> = flow {
//        val productIds = repository.getCartProductIds().first()
//        val products = productIds.mapNotNull { id -> repository.getProductById(id).firstOrNull() }
//        _cartProducts.value = products
//        emit(products)
//    }.flowOn(Dispatchers.IO)

    // Обновляем метод loadCartProducts
    fun loadCartProducts() {
        viewModelScope.launch {
            val productIds = repository.getCartProductIds().first()
            val productsWithCount = productIds.mapNotNull { id ->
                val product = repository.getProductById(id).firstOrNull()
                val count = repository.getCartProduct(id).firstOrNull()?.count ?: 0
                if (product != null && count > 0) {
                    CartProduct(product, count)
                } else {
                    null
                }
            }
            _cartProducts.value = productsWithCount
        }
    }
    // Добавляем метод для получения общей суммы
    fun calculateTotalAmount(): Double {
        return _cartProducts.value.sumOf { item ->
            item.product.getPriceDouble() * item.count
        }
    }

    fun addCart(productId: Int) {
        viewModelScope.launch {
            repository.addProductToCart(productId)
        }
    }

    fun toggleCart(productId: Int) {
        viewModelScope.launch {
            val isCart = repository.isCart(productId).first()
            if (isCart) {
                repository.removeProductFromCart(productId)
            } else {
//                repository.addCart(CartEntity(productId, count = 1))
            }
        }
    }

    fun incrementCount(productId: Int) {
        viewModelScope.launch {
            repository.increaseProductCount(productId)
        }
    }

    fun decrementCount(productId: Int) {
        viewModelScope.launch {
            repository.decreaseProductCount(productId)
            loadCartProducts()
        }
    }

    fun removeCart(productId: Int) {
        viewModelScope.launch {
            repository.removeProductFromCart(productId)
            loadCartProducts()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            loadCartProducts() // Обновляем список после очистки
        }
    }

}
