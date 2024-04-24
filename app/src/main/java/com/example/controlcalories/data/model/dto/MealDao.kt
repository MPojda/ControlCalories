package com.example.controlcalories.data.model.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: Meal): Long

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Update
    suspend fun updateProductInMeal(product: UserProduct)

    @Query("DELETE FROM user_products WHERE mealId = :mealId")
    suspend fun deleteAllProductsForMeal(mealId: Int)

    @Transaction
    @Query("SELECT * FROM meals WHERE dayOfWeek = :dayOfWeek ORDER BY mealNumber")
    fun getMealsByDay(dayOfWeek: String): Flow<List<Meal>>

    @Transaction
    @Query("SELECT * FROM user_products WHERE mealId = :mealId")
    fun getProductsForMeal(mealId: Int): Flow<List<UserProduct>>
}
