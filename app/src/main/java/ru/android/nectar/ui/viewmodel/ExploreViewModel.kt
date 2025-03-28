package ru.android.nectar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.repository.ExploreRepository
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: ExploreRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<ProductEntity>>(emptyList())
    val searchResults: StateFlow<List<ProductEntity>> = _searchResults.asStateFlow()

    fun searchProducts(query: String) {
        viewModelScope.launch {
            repository.searchProducts(query)
                .collect { products ->
                    _searchResults.value = products
                }
        }
    }
}
