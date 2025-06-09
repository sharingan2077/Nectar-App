//package ru.android.nectar.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import ru.android.nectar.data.local.entity.ProductEntity
//import ru.android.nectar.data.repository.ExploreRepository
//import ru.android.nectar.data.repository.FoodProduct
//import javax.inject.Inject
//
//
//sealed class SearchState {
//    object Loading : SearchState()
//    data class Success(val products: List<FoodProduct>) : SearchState()
//    object Error : SearchState()
//    object Empty : SearchState()
//}
//
//@HiltViewModel
//class SearchViewModel@Inject constructor(
//    private val repository: ExploreRepository
//) : ViewModel() {
//
//    private val _searchResults = MutableStateFlow<List<ProductEntity>>(emptyList())
//    val searchResults: StateFlow<List<ProductEntity>> = _searchResults.asStateFlow()
//
//    fun searchProducts(query: String) {
//        viewModelScope.launch {
//            repository.searchProducts(query)
//                .collect { products ->
//                    _searchResults.value = products
//                }
//        }
//    }
//}
//
////@HiltViewModel
////class SearchViewModel @Inject constructor(
////    private val repository: ExploreRepository
////) : ViewModel() {
////
////    private val _searchState = MutableStateFlow<SearchState>(SearchState.Success(emptyList()))
////    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()
////
////    private var lastQuery: String = ""
////
////    fun searchProducts(query: String) {
////        lastQuery = query
////        _searchState.value = SearchState.Loading
////
////        viewModelScope.launch {
////            repository.searchProducts(query, "new").collect { products ->
////                _searchState.value = when {
////                    products.isEmpty() -> SearchState.Empty
////                    else -> SearchState.Success(products)
////                }
////            }
////        }
////    }
////
////    fun retryLastSearch() {
////        searchProducts(lastQuery)
////    }
////}