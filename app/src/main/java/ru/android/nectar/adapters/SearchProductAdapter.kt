package ru.android.nectar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.android.nectar.data.repository.FoodProduct
import ru.android.nectar.databinding.ItemProductBinding

class SearchProductAdapter(
    private var products: List<FoodProduct> = emptyList()
) : RecyclerView.Adapter<SearchProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: FoodProduct) {
            binding.tvName.text = product.product_name
            Glide.with(binding.root).load(product.image_url).into(binding.imgProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<FoodProduct>) {
        products = newProducts
        notifyDataSetChanged()
    }
}