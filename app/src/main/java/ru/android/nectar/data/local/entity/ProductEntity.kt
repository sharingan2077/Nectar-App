package ru.android.nectar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageRes: Int,
    val name: String,
    val spec: String,
    val price: String,
    val category: String,
    val productType: String
)
