package ru.android.nectar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.repository.ExploreRepository
import javax.inject.Inject


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}


@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: ExploreRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<Resource<List<ProductEntity>>>(Resource.Loading())
    val searchResults: StateFlow<Resource<List<ProductEntity>>> = _searchResults.asStateFlow()

    private val _productsByType = MutableStateFlow<Resource<List<ProductEntity>>>(Resource.Loading())
    val productsByType: StateFlow<Resource<List<ProductEntity>>> = _productsByType.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<ProductEntity>>(emptyList())
    val searchHistory: StateFlow<List<ProductEntity>> = _searchHistory.asStateFlow()

    fun getProductsByType(type: String) {
        viewModelScope.launch {
            _productsByType.value = Resource.Loading()
            try {
                val products = repository.getProductsByType(type).first()
                _productsByType.value = Resource.Success(products)
            } catch (e: Exception) {
                _productsByType.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _searchResults.value = Resource.Loading()
            try {
                val products = repository.searchProducts(query).first()
                _searchResults.value = Resource.Success(products)
            } catch (e: Exception) {
                _searchResults.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadSearchHistory() {
        viewModelScope.launch {
            val history = repository.getSearchHistory()
            _searchHistory.value = history.toList() // создаём новый список
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
            _searchHistory.value = emptyList()
        }
    }

    fun addProductToHistory(product: ProductEntity) {
        viewModelScope.launch {
            repository.addToSearchHistory(product.id)
            loadSearchHistory()
        }
    }
}

