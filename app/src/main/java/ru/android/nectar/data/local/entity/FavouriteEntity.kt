package ru.android.nectar.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

// Избранное
@Entity(
    tableName = "favorite_products",
    primaryKeys = ["productId"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
        )
    ]
)
data class FavouriteEntity(
    val productId: Int
)