package ru.android.nectar.ui.shop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.android.nectar.adapters.ProductAdapter
import ru.android.nectar.databinding.FragmentShopBinding
import ru.android.nectar.ui.favourite.FavouriteViewModel

private const val TAG = "ShopFragment"

@AndroidEntryPoint
class ShopFragment : Fragment() {


    private lateinit var binding: FragmentShopBinding
    private val viewModel: ShopViewModel by viewModels()
    private val favouriteViewModel: FavouriteViewModel by viewModels()
    private lateinit var adapterProduct: ProductAdapter
    private lateinit var adapterExclusive: ProductAdapter
    private lateinit var adapterBestSelling: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)

//        viewModel.refreshProducts(dataProductList)

        setupRecyclerView()
        observeData()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapterProduct = ProductAdapter(
            emptyList(),
            onFavoriteCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    favouriteViewModel.isFavorite(1, product.id).collect { isFav ->
                        callback(isFav) // Передаем результат обратно в адаптер
                    }
                }
            },
            onFavoriteClick = { product ->
                favouriteViewModel.toggleFavorite(1, product.id) // Меняем статус
            }
        )

        binding.rvAll.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAll.adapter = adapterProduct

        adapterExclusive = ProductAdapter(
            emptyList(),
            onFavoriteCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    favouriteViewModel.isFavorite(1, product.id).collect { isFav ->
                        callback(isFav)
                    }
                }
            },
            onFavoriteClick = { product ->
                Log.d(TAG, "adapterExclusive onFavouriteClick -> ${product.name}")
                favouriteViewModel.toggleFavorite(1, product.id)
            }
        )

        binding.rvExclusive.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvExclusive.adapter = adapterExclusive

        adapterBestSelling = ProductAdapter(
            emptyList(),
            onFavoriteCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    favouriteViewModel.isFavorite(1, product.id).collect { isFav ->
                        callback(isFav)
                    }
                }
            },
            onFavoriteClick = { product ->
                favouriteViewModel.toggleFavorite(1, product.id)
            }
        )

        binding.rvBestSelling.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBestSelling.adapter = adapterBestSelling
    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collect { products ->
                Log.d(TAG, "All products -> $products")
                adapterProduct.updateData(products)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsExclusive.collect { products ->
                Log.d(TAG, "Exclusive products -> $products")
                adapterExclusive.updateData(products)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsBestSelling.collect { products ->
                Log.d(TAG, "Best Selling products -> $products")
                adapterBestSelling.updateData(products)
            }
        }
    }
}