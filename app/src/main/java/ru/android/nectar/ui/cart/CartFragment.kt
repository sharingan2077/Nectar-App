package ru.android.nectar.ui.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.android.nectar.R
import ru.android.nectar.adapters.ProductAdapter
import ru.android.nectar.databinding.FragmentCartBinding
import ru.android.nectar.databinding.FragmentShopBinding
import ru.android.nectar.ui.favourite.FavouriteViewModel
import ru.android.nectar.ui.shop.ShopViewModel

private const val TAG = "CartFragment"

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: ShopViewModel by activityViewModels()
    private val favouriteViewModel: FavouriteViewModel by activityViewModels()
    private lateinit var adapterProduct: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)

        setupRecyclerView()
        observeData()
        binding.btnBuy.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_orderAcceptedFragment)
        }
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

        binding.rvCart.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCart.adapter = adapterProduct

    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collect { products ->
                Log.d(TAG, "All products -> $products")
                adapterProduct.updateData(products)
            }
        }

    }


}