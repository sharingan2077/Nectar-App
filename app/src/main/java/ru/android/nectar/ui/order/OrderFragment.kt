package ru.android.nectar.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.android.nectar.R
import ru.android.nectar.adapters.OrderAdapter
import ru.android.nectar.databinding.FragmentOrderBinding
import ru.android.nectar.ui.viewmodel.OrderViewModel


@AndroidEntryPoint
class OrderFragment : Fragment() {

    private val viewModel: OrderViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter

    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        binding.btnGoToShop.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_shopFragment)
        }
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter()
        binding.ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        // Наблюдаем за заказами
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orders.collect { orders ->
                val sortedOrders = orders.sortedByDescending { it.order.orderDate }
                orderAdapter.submitList(sortedOrders)
                binding.emptyState.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Наблюдаем за статусом загрузки
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // Наблюдаем за ошибками
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                if (error != null) {
                    binding.errorText.text = error
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.errorText.visibility = View.GONE
                }
            }
        }
    }


    fun refreshOrders() {
        viewModel.loadOrders()
    }

}