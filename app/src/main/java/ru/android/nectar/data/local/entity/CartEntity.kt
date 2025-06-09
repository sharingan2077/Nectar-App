package ru.android.nectar.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

// Корзина
@Entity(
    tableName = "cart_products",
    primaryKeys = ["productId"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
        )
    ]
)
data class CartEntity(
    val productId: Int,
    val count: Int = 1
)