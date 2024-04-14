package com.example.controlcalories.data.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_products")
data class UserProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val calories: Int,
    val protein: Float,
    val fat: Float,
    val carbohydrates: Float,
    val sugar: Float,
    val fiber: Float,
    val addedDate: Long = System.currentTimeMillis(),
    val weight: Float,
    val categoryId: Int
)