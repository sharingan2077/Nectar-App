package ru.android.nectar.ui.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.android.nectar.R
import ru.android.nectar.adapters.ProductCartAdapter
import ru.android.nectar.databinding.FragmentCartBinding
import ru.android.nectar.ui.viewmodel.ShopViewModel
import ru.android.nectar.ui.viewmodel.CartViewModel

private const val TAG = "CartFragment"

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: ShopViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapterCartProduct: ProductCartAdapter

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
        adapterCartProduct = ProductCartAdapter(
            countCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    cartViewModel.getCartProduct(1, product.id).collect { cartEntity ->
                        cartEntity?.let {
                            callback(it.count)
                        }
                    }
                }
            },
            incrementProduct = { product ->
                cartViewModel.incrementCount(userId = 1, productId = product.id)
            },
            decrementProduct = { product ->
                cartViewModel.decrementCount(userId = 1, productId = product.id)
            },
            removeProduct = { product ->
                Log.d(TAG, "Remove product -> $product")
                cartViewModel.removeCart(userId = 1, productId = product.id)
            }

            )

        binding.rvCart.layoutManager = LinearLayoutManager(context)
        binding.rvCart.adapter = adapterCartProduct

    }

    private fun observeData() {
        cartViewModel.loadCartProducts(1)
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProducts.collectLatest { products ->
                Log.d(TAG, "Received products: $products")
                adapterCartProduct.submitList(products)
            }
        }

    }


}