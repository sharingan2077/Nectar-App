package ru.android.nectar.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.databinding.ItemProductBinding
import ru.android.nectar.databinding.ItemProductCartBinding
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale

private const val TAG = "ProductCartAdapter"

class ProductCartAdapter(
    private val countCheck: (ProductEntity, (Int) -> Unit) -> Unit,
    private val incrementProduct: (ProductEntity) -> Unit,
    private val decrementProduct: (ProductEntity) -> Unit,
    private val removeProduct: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, ProductCartAdapter.ProductCartViewHolder>(ProductDiffCallback()) {

    inner class ProductCartViewHolder(private val binding: ItemProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            Log.d(TAG, "bind $product")
            binding.tvName.text = product.name
            binding.tvSpec.text = product.spec
            binding.imgProduct.setImageResource(product.imageRes)

            countCheck(product) { count ->
                val formattedCount = String.format(Locale.getDefault(), "%d", count)
                binding.tvCount.text = formattedCount

//                val price = BigDecimal(binding.tvPrice.text.toString().removePrefix("$"))
//                val result = price.multiply(BigDecimal(count))
//                val formattedResult = result.setScale(2, RoundingMode.HALF_UP)
                val price = product.price.removePrefix("$").toDouble()
                val result = price * count
                val decimalFormat = DecimalFormat("#,##0.00")
                val formattedPrice = "$" + decimalFormat.format(result)
                binding.tvPrice.text = formattedPrice
            }

            binding.btnIncrease.setOnClickListener {
                incrementProduct(product)
            }
            binding.btnDecrease.setOnClickListener {
                decrementProduct(product)
            }
            binding.imgRemove.setOnClickListener {
                removeProduct(product)
            }


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductCartAdapter.ProductCartViewHolder {
        val binding =
            ItemProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductCartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductCartAdapter.ProductCartViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
//    fun updateData(newProducts: List<ProductEntity>) {
//        products = newProducts
//        notifyDataSetChanged()
//    }
}