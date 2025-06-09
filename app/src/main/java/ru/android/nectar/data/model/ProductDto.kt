package ru.android.nectar.data.model

import ru.android.nectar.data.local.entity.ProductEntity

data class ProductResponse(
    val id: Int,
    val imageName: String,
    val name: String,
    val spec: String,
    val price: String,
    val category: String,
    val productType: String
)

fun ProductResponse.toProductEntity() = ProductEntity(
    id = id,
    imageName = imageName,
    name = name,
    spec = spec,
    price = price,
    category = category,
    productType = productType
)


fun OrderItemResponse.toProductEntity() = ProductEntity(
    id = product.id,
    imageName = product.imageName,
    name = product.name,
    spec = product.spec,
    price = product.price,
    category = product.category,
    productType = product.productType
)
