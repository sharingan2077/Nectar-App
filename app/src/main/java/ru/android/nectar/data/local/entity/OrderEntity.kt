package ru.android.nectar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderDate: Long, // System.currentTimeMillis()
    val totalAmount: Double,
    val deliveryAddress: String? = null
)