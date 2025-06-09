package ru.android.nectar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageName: String,
    val name: String,
    val spec: String,
    val price: String,
    val category: String,
    val productType: String
) {
    // Добавляем метод для безопасного преобразования цены
    fun getPriceDouble(): Double {
        return price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
    }
}