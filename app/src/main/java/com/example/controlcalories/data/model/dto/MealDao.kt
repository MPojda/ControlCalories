package com.example.controlcalories.data.model.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: Meal): Long

    @Query("DELETE FROM meals WHERE mealId = :mealId")
    suspend fun deleteMeal(mealId: Int)

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

    @Query("SELECT * FROM meals WHERE dateFor BETWEEN :startOfDay AND :endOfDay")
    fun getMealsByDate(startOfDay: String, endOfDay: String): Flow<List<Meal>>

    @Transaction
    @Query("SELECT * FROM meals WHERE mealId = :mealId")
    fun getMealWithProducts(mealId: Int): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE mealId = :mealId")
    fun getMealById(mealId: Int): Flow<List<Meal>>
}

