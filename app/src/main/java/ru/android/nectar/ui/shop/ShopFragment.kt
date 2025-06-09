package ru.android.nectar.ui.shop

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.android.nectar.R
import ru.android.nectar.adapters.BannerAdapter
import ru.android.nectar.adapters.ProductAdapter
import ru.android.nectar.databinding.FragmentShopBinding
import ru.android.nectar.example.dataProductList
import ru.android.nectar.ui.viewmodel.CartViewModel
import ru.android.nectar.ui.viewmodel.FavouriteViewModel
import ru.android.nectar.ui.viewmodel.ShopViewModel

private const val TAG = "ShopFragment"

@AndroidEntryPoint
class ShopFragment : Fragment() {


    private lateinit var binding: FragmentShopBinding
    private val viewModel: ShopViewModel by viewModels()
    private val favouriteViewModel: FavouriteViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var adapterProduct: ProductAdapter
    private lateinit var adapterExclusive: ProductAdapter
    private lateinit var adapterBestSelling: ProductAdapter


    private lateinit var adapter: BannerAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val banners = listOf(
        R.drawable.img_banner_1,
        R.drawable.img_banner_2,
        R.drawable.img_banner_3
    )

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val nextItem = (binding.bannerViewPager.currentItem + 1) % banners.size
            binding.bannerViewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(this, 6000)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)


//        refreshDatabase()

        startFragment()
//
        return binding.root
    }

    private fun refreshDatabase() {
        viewModel.refreshProducts(dataProductList)
//        viewModel.addProductsIfEmpty()
        viewModel.uploadLocalChanges()
    }
    private fun startFragment() {
        viewModel.performSync()
        setupSyncObserver()

        setupRecyclerView()
        observeData()

        binding.dotsIndicator.attachTo(binding.bannerViewPager)
        handler.postDelayed(autoScrollRunnable, 4000)
    }



    private fun setupRecyclerView() {
        adapter = BannerAdapter(banners)
        binding.bannerViewPager.adapter = adapter

        adapterProduct = ProductAdapter(
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
                        callback(isFav) // Передаем результат обратно в адаптер
                    }
                }
            },
            onFavoriteClick = { product ->
                favouriteViewModel.toggleFavorite(product.id) // Меняем статус
            }
        )

        binding.rvAll.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAll.adapter = adapterProduct

        adapterExclusive = ProductAdapter(
            onCartCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    cartViewModel.isCart(product.id).collect { isCart ->
                        callback(isCart)
                    }
                }
            },
            onCartClick = { product ->
                Log.d(TAG, "adapterExclusive onCartClick -> ${product.name}")
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
                Log.d(TAG, "adapterExclusive onFavouriteClick -> ${product.name}")
                favouriteViewModel.toggleFavorite(product.id)
            }
        )

        binding.rvExclusive.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvExclusive.adapter = adapterExclusive

        adapterBestSelling = ProductAdapter(
            onCartCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    cartViewModel.isCart(product.id).collect { isCart ->
                        callback(isCart)
                    }
                }
            },
            onCartClick = { product ->
                Log.d(TAG, "adapterBestSelling onCartClick -> ${product.name}")
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
                adapterProduct.submitList(products)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsExclusive.collect { products ->
                Log.d(TAG, "Exclusive products -> $products")
                adapterExclusive.submitList(products)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsBestSelling.collect { products ->
                Log.d(TAG, "Best Selling products -> $products")
                adapterBestSelling.submitList(products)
            }
        }
    }

    private fun setupSyncObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.syncState.collect { state ->
                when (state) {
                    is ShopViewModel.SyncState.Loading -> {
                        // Показываем индикатор загрузки
                        binding.pbProducts.visibility = View.VISIBLE
                    }
                    is ShopViewModel.SyncState.Success -> {
                        binding.pbProducts.visibility = View.GONE
                        // Данные автоматически обновятся через observeData()
                    }
                    is ShopViewModel.SyncState.Error -> {
                        binding.pbProducts.visibility = View.GONE
                        // Показываем ошибку только если это не отсутствие интернета
                        if (!state.message.contains("internet", ignoreCase = true)) {
//                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(autoScrollRunnable)
    }
}