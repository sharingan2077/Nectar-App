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

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private val viewModel: FavouriteViewModel by activityViewModels()
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
            products = emptyList(),
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getFavoriteProductEntities(1).collectLatest { products ->
                adapter.updateData(products)
            }
        }
    }

}