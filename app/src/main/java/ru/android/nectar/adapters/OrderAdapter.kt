package ru.android.nectar.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.android.nectar.data.local.dao.OrderItemWithProduct
import ru.android.nectar.data.local.dao.OrderWithItems
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "OrderAdapter"

class OrderAdapter : ListAdapter<OrderWithItems, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val displayNumber = currentList.size - position
        holder.bind(getItem(position), displayNumber)
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderWithItems: OrderWithItems, displayNumber: Int) {
            val order = orderWithItems.order
            val items = orderWithItems.items

            binding.apply {
                orderNumber.text = "Заказ #$displayNumber"
                orderDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                    .format(Date(order.orderDate))
                orderTotal.text = "%.2f ₽".format(order.totalAmount)
                orderAddress.text = order.deliveryAddress ?: "Самовывоз"

                val productsAdapter = OrderProductAdapter()
                orderProductsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = productsAdapter
                    setHasFixedSize(true)
                }
                orderProductsRecyclerView.isNestedScrollingEnabled = false

                productsAdapter.submitList(items)
            }
        }
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<OrderWithItems>() {
    override fun areItemsTheSame(oldItem: OrderWithItems, newItem: OrderWithItems): Boolean {
        return oldItem.order.id == newItem.order.id
    }

    override fun areContentsTheSame(oldItem: OrderWithItems, newItem: OrderWithItems): Boolean {
        return oldItem == newItem
    }
}