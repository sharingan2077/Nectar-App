package ru.android.nectar.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.repository.ProductRepository
import javax.inject.Inject


@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    val products: StateFlow<List<ProductEntity>> = repository.getProducts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val productsExclusive: StateFlow<List<ProductEntity>> = repository.getProductsByCategory("exclusive")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val productsBestSelling: StateFlow<List<ProductEntity>> = repository.getProductsByCategory("best selling")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertProducts(newProducts: List<ProductEntity>) {
        viewModelScope.launch {
            repository.insertProducts(newProducts)
        }
    }

    fun addProductsIfEmpty() {
        viewModelScope.launch {
            if (repository.isProductTableIsEmpty()) {
                refreshProducts(dataProductList)
            }
        }
    }

    fun refreshProducts(newProducts: List<ProductEntity>) {
        viewModelScope.launch {
            repository.refreshProducts(newProducts)
        }
    }
}