package ru.android.nectar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.android.nectar.R
import ru.android.nectar.data.local.dao.OrderItemWithProduct
import ru.android.nectar.databinding.ItemOrderProductBinding
import ru.android.nectar.utils.getDrawableIdByName

class OrderProductAdapter : ListAdapter<OrderItemWithProduct, OrderProductAdapter.ViewHolder>(
    OrderProductDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemOrderProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderItemWithProduct) {
            binding.apply {
                productName.text = item.product.name
                productQuantity.text = "${item.orderItem.quantity} шт."
                val resId = getDrawableIdByName(root.context, item.product.imageName)
                productImage.setImageResource(if (resId != 0) resId else R.drawable.img_fruit_banana)
                val rawPrice = item.product.price.replace("[^\\d.,]".toRegex(), "").replace(",", ".")
                val priceDouble = rawPrice.toDouble()
                productPrice.text = "%.2f ₽".format(priceDouble)
            }
        }
    }
}

class OrderProductDiffCallback : DiffUtil.ItemCallback<OrderItemWithProduct>() {
    override fun areItemsTheSame(oldItem: OrderItemWithProduct, newItem: OrderItemWithProduct): Boolean {
        return oldItem.orderItem.productId == newItem.orderItem.productId
    }

    override fun areContentsTheSame(oldItem: OrderItemWithProduct, newItem: OrderItemWithProduct): Boolean {
        return oldItem == newItem
    }
}