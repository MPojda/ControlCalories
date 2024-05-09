package com.example.controlcalories.data.model.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Int = 0,
    val name: String,
    val dayOfWeek: String,
    val mealNumber: Int,
    val dateFor: String
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
    val dateFor: String,
    val weight: Float,
    val categoryId: Int,
    val mealId: Int
)

data class TotalsForDay(
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbohydrates: Double
)

data class MealWithProducts(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "mealId"
    )
    val products: List<UserProduct>
)


@Entity(primaryKeys = ["mealId", "productId"])
data class MealProductCrossRef(
    val mealId: Int,
    val productId: Int
)