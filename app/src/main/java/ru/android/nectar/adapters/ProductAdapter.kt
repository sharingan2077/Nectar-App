package ru.android.nectar.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.android.nectar.R
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.databinding.ItemProductBinding

private const val TAG = "ProductAdapter"
class ProductAdapter(
    private var products: List<ProductEntity>,
    private val onFavoriteCheck: (ProductEntity, (Boolean) -> Unit) -> Unit, // Запрос избранного
    private val onFavoriteClick: (ProductEntity) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.imgProduct.setImageResource(product.imageRes)
            binding.tvName.text = product.name
            binding.tvSpec.text = product.spec
            binding.tvPrice.text = product.price

            binding.btnAdd.setOnClickListener {
                // Обработчик нажатия на кнопку добавления
            }

            // Проверяем, является ли продукт в избранном
//            viewModel.isFavorite(1, product.id).collect { isFav ->
//                if (isFav) {
//                    binding.imgProduct.setColorFilter(
//                        ContextCompat.getColor(
//                            binding.root.context,
//                            R.color.red
//                        )
//                    )
//                } else {
//                    binding.imgProduct.clearColorFilter()
//                }
//            }
//
//
//            binding.imgProduct.setOnClickListener {
//                // Переключаем статус избранного
//                viewModel.toggleFavorite(1, product.id)
//            }

            // Запрос на проверку избранного (ответ придёт в колбэк)
            onFavoriteCheck(product) { isFav ->
                if (isFav) {
                    binding.imgFavourite.setColorFilter(
                        ContextCompat.getColor(binding.root.context, R.color.red)
                    )
                } else {
                    binding.imgFavourite.clearColorFilter()
                }
            }

            // Обработчик клика на кнопку избранного
            binding.imgFavourite.setOnClickListener {
                Log.d(TAG, "adapter onFavouriteClick -> ${product.name}")
                onFavoriteClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun updateData(newProducts: List<ProductEntity>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
