package ru.android.nectar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Уникальный ID
    val phoneNumber: String, // Номер телефона
    val profileName: String, // Имя профиля
    val latitude: Double?, // Широта (может быть null, если геолокация не задана)
    val longitude: Double? // Долгота
)

