package ru.android.nectar.adapters

import android.animation.ObjectAnimator
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.android.nectar.R
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.databinding.ItemProductBinding


private const val TAG = "ProductAdapter"

class ProductAdapter(
    private val onFavoriteCheck: (ProductEntity, (Boolean) -> Unit) -> Unit, // Запрос избранного
    private val onFavoriteClick: (ProductEntity) -> Unit,
    private val onCartCheck: (ProductEntity, (Boolean) -> Unit) -> Unit, // Запрос избранного
    private val onCartClick: (ProductEntity) -> Unit,
    private val onProductClick: (ProductEntity) -> Unit = {}//Добавление в историю
) : ListAdapter<ProductEntity, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.imgProduct.setImageResource(product.imageRes)
            binding.tvName.text = product.name
            binding.tvSpec.text = product.spec
            binding.tvPrice.text = product.price

            binding.btnAdd.setOnClickListener {
                // Обработчик нажатия на кнопку добавления
                Log.d(TAG, "adapter onCartClick -> ${product.name}")
                onCartClick(product)
            }
            // Запрос на проверку товара в корзине (ответ придёт в колбэк)
            onCartCheck(product) { isCart ->
                if (isCart) {
                    binding.btnAdd.setImageResource(R.drawable.ic_in_cart_3)
                } else {
                    binding.btnAdd.setImageResource(R.drawable.ic_add)
                }
            }

            // Запрос на проверку избранного (ответ придёт в колбэк)
            onFavoriteCheck(product) { isFav ->
                if (isFav) {
                    binding.imgFavourite.setImageResource(R.drawable.ic_favourite_red)
                } else {
                    binding.imgFavourite.setImageResource(R.drawable.ic_favourite)
                }
            }

            // Обработчик клика на кнопку избранного
            binding.imgFavourite.setOnClickListener {
                Log.d(TAG, "adapter onFavouriteClick -> ${product.name}")
                onFavoriteClick(product)
            }

            binding.root.setOnClickListener {
                onProductClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

}
