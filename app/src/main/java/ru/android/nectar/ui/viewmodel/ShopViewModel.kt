package ru.android.nectar.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.repository.ProductRepository
import ru.android.nectar.example.dataProductList
import javax.inject.Inject

private const val TAG = "ShopViewModel"

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    // Текущие состояния
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState

    sealed class SyncState {
        object Idle : SyncState()
        object Loading : SyncState()
        object Success : SyncState()
        data class Error(val message: String) : SyncState()
    }

    fun performSync() {
        viewModelScope.launch {
            _syncState.value = SyncState.Loading
            try {
                // 1. Сначала загружаем данные с сервера (если есть интернет)
                repository.syncWithServer()
                // 2. Затем отправляем локальные изменения (если есть)
//                repository.uploadLocalChanges()
                _syncState.value = SyncState.Success
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e.message ?: "Sync failed")
            }
        }
    }


    val products: StateFlow<List<ProductEntity>> = repository.getProducts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val productsExclusive: StateFlow<List<ProductEntity>> = repository.getProductsByCategory("эксклюзив")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val productsBestSelling: StateFlow<List<ProductEntity>> = repository.getProductsByCategory("хит")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertProducts(newProducts: List<ProductEntity>) {
        viewModelScope.launch {
            repository.insertProducts(newProducts)
        }
    }

    fun addProductsIfEmpty() {
        viewModelScope.launch {
            if (repository.isProductTableIsEmpty()) {
                Log.d(TAG, "productTableIsEmpty")
                refreshProducts(dataProductList)
            }
        }
    }


    fun refreshProducts(newProducts: List<ProductEntity>) {
        viewModelScope.launch {
            repository.refreshProducts(newProducts)
        }
    }

    fun syncWithServer() {
        viewModelScope.launch {
            try {
                repository.syncWithServer()
                // Оповестить UI об успешной синхронизации
            } catch (e: Exception) {
                // Обработать ошибку
            }
        }
    }

    fun uploadLocalChanges() {
        viewModelScope.launch {
            try {
                repository.uploadLocalChanges()
                // Оповестить UI об успешной загрузке
            } catch (e: Exception) {
                Log.d(TAG, e.message ?: "Exception in uploadLocalChanges")
                // Обработать ошибку
            }
        }
    }
}