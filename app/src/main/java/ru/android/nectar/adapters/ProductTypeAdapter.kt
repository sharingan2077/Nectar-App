package ru.android.nectar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.android.nectar.databinding.ItemProductTypeBinding

class ProductTypeAdapter(private val productTypes: List<Pair<String, Int>>,
                         private val onItemClick: (String) -> Unit)
    : RecyclerView.Adapter<ProductTypeAdapter.ProductTypeViewHolder>() {

    inner class ProductTypeViewHolder(private val binding: ItemProductTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Pair<String, Int>) {
            binding.tvProductType.text = product.first
            binding.imgProductType.setImageResource(product.second)
            binding.root.setOnClickListener {
                onItemClick(product.first)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductTypeViewHolder {
        val binding = ItemProductTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductTypeViewHolder, position: Int) {
        holder.bind(productTypes[position])
    }

    override fun getItemCount() = productTypes.size

}