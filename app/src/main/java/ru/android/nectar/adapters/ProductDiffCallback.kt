package ru.android.nectar.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.android.nectar.data.local.entity.ProductEntity

class ProductDiffCallback : DiffUtil.ItemCallback<ProductEntity>() {

    // Проверяем, являются ли два элемента одним и тем же объектом (по ID или уникальному полю)
    override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
        return oldItem.id == newItem.id // Допустим, что ID уникален для каждого продукта
    }

    // Проверяем, изменилось ли содержимое элемента (например, изменился ли count)
    override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
        return oldItem == newItem // Сравниваем объекты полностью
    }
}
