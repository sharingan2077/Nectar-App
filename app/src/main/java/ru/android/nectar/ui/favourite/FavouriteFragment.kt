package ru.android.nectar.ui.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.android.nectar.adapters.ProductAdapter
import ru.android.nectar.databinding.FragmentFavouriteBinding
import ru.android.nectar.ui.viewmodel.CartViewModel
import ru.android.nectar.ui.viewmodel.FavouriteViewModel

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private val viewModel: FavouriteViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            onCartCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    cartViewModel.isCart(1, product.id).collect { isCart ->
                        callback(isCart)
                    }
                }
            },
            onCartClick = { product ->
                cartViewModel.addCart(1, product.id)
            },
            onFavoriteCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.isFavorite(1, product.id).collect { isFav ->
                        callback(isFav)
                    }
                }
            },
            onFavoriteClick = { product ->
                viewModel.toggleFavorite(1, product.id)
            }
        )

        binding.rvFavourite.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@FavouriteFragment.adapter
        }

    }

    private fun observeData() {
        viewModel.loadFavouriteProducts(1)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favouriteProducts.collectLatest { products ->
                adapter.submitList(products)
            }
        }
    }

}