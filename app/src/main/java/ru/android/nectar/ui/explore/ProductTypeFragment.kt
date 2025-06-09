package ru.android.nectar.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.android.nectar.adapters.ProductAdapter
import ru.android.nectar.databinding.FragmentProductTypeBinding
import ru.android.nectar.ui.viewmodel.CartViewModel
import ru.android.nectar.ui.viewmodel.ExploreViewModel
import ru.android.nectar.ui.viewmodel.FavouriteViewModel
import ru.android.nectar.ui.viewmodel.Resource


@AndroidEntryPoint
class ProductTypeFragment : Fragment() {
    private lateinit var binding: FragmentProductTypeBinding
    private lateinit var productsAdapter: ProductAdapter // Адаптер для продуктов
    private val exploreViewModel: ExploreViewModel by activityViewModels()
    private val favouriteViewModel: FavouriteViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductTypeBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val categoryName = arguments?.getString("categoryName") ?: return

        // Устанавливаем заголовок
        binding.tvCategoryTitle.text = categoryName
        exploreViewModel.getProductsByType(categoryName)

        // Загружаем продукты по категории
        lifecycleScope.launch {
            exploreViewModel.productsByType
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> showLoading()
                        is Resource.Success -> {
                            resource.data?.let(productsAdapter::submitList)
                            hideLoading()
                        }
                        is Resource.Error -> {
                            showError(resource.message)
                            hideLoading()
                        }
                    }
                }
        }

    }

    private fun setupRecyclerView() {
        productsAdapter = ProductAdapter(
            onCartCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    cartViewModel.isCart(product.id).collect { isCart ->
                        callback(isCart)
                    }
                }
            },
            onCartClick = { product ->
                cartViewModel.addCart(product.id)
            },
            onFavoriteCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    favouriteViewModel.isFavorite(product.id).collect { isFav ->
                        callback(isFav)
                    }
                }
            },
            onFavoriteClick = { product ->
                favouriteViewModel.toggleFavorite(product.id)
            },
        )

        binding.rvProductsInCategory.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@ProductTypeFragment.productsAdapter
        }

    }




    private fun showLoading() { /* ... */ }
    private fun hideLoading() { /* ... */ }
    private fun showError(message: String?) { /* ... */ }
}