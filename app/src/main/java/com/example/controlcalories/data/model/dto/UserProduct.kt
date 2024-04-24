package com.example.controlcalories.data.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Int = 0,
    val name: String,
    val dayOfWeek: String,
    val mealNumber: Int
)

@Entity(tableName = "user_products")
data class UserProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbohydrates: Double,
    val sugar: Double,
    val fiber: Double,
    val addedDate: Long = System.currentTimeMillis(),
    val weight: Float,
    val categoryId: Int,
    val mealId: Int
)

@Entity(primaryKeys = ["mealId", "productId"])
data class MealProductCrossRef(
    val mealId: Int,
    val productId: Int
)