package com.example.controlcalories.data.model.dto


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey val uid: Int,
    val name: String,
    val calories: Int,
    val protein: Float,
    val fat: Float,
    val carbohydrates: Float,
    val sugar: Float,
    val fiber: Float
)