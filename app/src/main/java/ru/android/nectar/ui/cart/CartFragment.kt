package ru.android.nectar.ui.cart

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.android.nectar.R
import ru.android.nectar.adapters.ProductCartAdapter
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.databinding.FragmentCartBinding
import ru.android.nectar.ui.viewmodel.ShopViewModel
import ru.android.nectar.ui.viewmodel.CartViewModel
import ru.android.nectar.ui.viewmodel.OrderViewModel

private const val TAG = "CartFragment"

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: ShopViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private lateinit var adapterCartProduct: ProductCartAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_CODE = 1001


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        setupRecyclerView()
        observeData()

        binding.btnBuy.setOnClickListener {
            checkLocationPermissionAndProceed()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return binding.root
    }

    private fun checkLocationPermissionAndProceed() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocationAndOrder()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndOrder()
            } else {
                showError("Невозможно оформить заказ без разрешения на местоположение")
            }
        }
    }

    private fun getCurrentLocationAndOrder() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val addressString = getAddressFromLocation(location)
                createOrderFromCart(addressString)
            } else {
                showError("Не удалось получить местоположение")
            }
        }
    }

    private fun getAddressFromLocation(location: Location): String {
        val geocoder = Geocoder(requireContext())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            "${address.locality ?: ""}, ${address.thoroughfare ?: ""} ${address.subThoroughfare ?: ""}"
        } else {
            "Неизвестный адрес"
        }
    }



    private fun setupRecyclerView() {
        adapterCartProduct = ProductCartAdapter(
            countCheck = { product, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    cartViewModel.getCartProduct(product.id).collect { cartEntity ->
                        cartEntity?.let {
                            callback(it.count)
                        }
                    }
                }
            },
            incrementProduct = { product ->
                cartViewModel.incrementCount(product.id)
            },
            decrementProduct = { product ->
                cartViewModel.decrementCount(product.id)
            },
            removeProduct = { product ->
                Log.d(TAG, "Remove product -> $product")
                cartViewModel.removeCart(product.id)
            }
        )

        binding.rvCart.layoutManager = LinearLayoutManager(context)
        binding.rvCart.adapter = adapterCartProduct
    }

    private fun observeData() {
        cartViewModel.loadCartProducts()
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProducts.collectLatest { products ->
                Log.d(TAG, "Received products: $products")
                adapterCartProduct.submitList(products.map { it.product })
            }
        }
    }

    private fun createOrderFromCart(deliveryAddress: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val cartItems = cartViewModel.cartProducts.value

                if (cartItems.isEmpty()) {
                    showError("Ваша корзина пуста")
                    return@launch
                }

                // Получаем общую сумму
                val totalAmount = cartViewModel.calculateTotalAmount()


                // Преобразуем в CartEntity для OrderRepository
                val cartEntities = cartItems.map {
                    CartEntity(productId = it.product.id, count = it.count)
                }

                // Создаем заказ
                orderViewModel.createOrder(
                    cartItems = cartEntities,
                    totalAmount = totalAmount,
                    deliveryAddress = deliveryAddress
                )

                // Очищаем корзину
                cartViewModel.clearCart()

                // Переходим на экран подтверждения
                findNavController().navigate(R.id.action_cartFragment_to_orderAcceptedFragment)

            } catch (e: Exception) {
                showError("Ошибка при создании заказа: ${e.localizedMessage}")
            }
        }
    }


    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
