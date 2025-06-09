package ru.android.nectar.data.model

import ru.android.nectar.data.local.entity.CartEntity

data class ProductCartResponseRemote(
    val id: Int,
    val count: Int
)

fun ProductCartResponseRemote.toEntity() = CartEntity(
    productId = this.id,
    count = this.count
)
