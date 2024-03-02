package com.example.controlcalories.data.model.dto


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey val id: String,
    val name: String,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbohydrates: Double,
    val sugar: Double,
    val fiber: Double
)