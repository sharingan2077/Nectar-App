package ru.android.nectar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.android.nectar.data.local.dao.OrderWithItems
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.data.repository.OrderRepository
import javax.inject.Inject

private const val TAG = "OrderViewModel"

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderWithItems>>(emptyList())
    val orders: StateFlow<List<OrderWithItems>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val orders = orderRepository.getOrderHistory()
                _orders.value = orders
            } catch (e: Exception) {
                _error.value = "Не удалось загрузить историю заказов: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createOrder(cartItems: List<CartEntity>, totalAmount: Double, deliveryAddress: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                orderRepository.createOrder(cartItems, totalAmount, deliveryAddress)
                loadOrders() // Обновляем список после создания нового заказа
            } catch (e: Exception) {
                _error.value = "Ошибка при создании заказа: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}