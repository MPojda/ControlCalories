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

    @Query("SELECT COUNT(*) FROM meals WHERE dateFor = :selectedDate")
    suspend fun getNumberOfMealsForDate(selectedDate: String): Int

    @Query("UPDATE meals SET mealNumber = :newNumber WHERE mealId = :mealId")
    suspend fun updateMealNumber(mealId: Int, newNumber: Int)

    @Query("SELECT * FROM meals WHERE dateFor = :selectedDate ORDER BY mealNumber")
    suspend fun getMealsForDate(selectedDate: String): List<Meal>

    @Transaction
    @Query("SELECT * FROM meals WHERE dayOfWeek = :dayOfWeek ORDER BY mealNumber")
    fun getMealsByDay(dayOfWeek: String): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE dateFor = :selectedDate")
    fun getMealsAndProductsForDate(selectedDate: String): Flow<List<MealWithProducts>>

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

    @Query("SELECT DISTINCT dateFor FROM meals ORDER BY dateFor")
    fun getAllMealDates(): Flow<List<String>>

    @Query("SELECT * FROM meals WHERE mealId = :mealId")
    suspend fun getMealByID(mealId: Int): Meal?

    @Query("SELECT * FROM meals WHERE dateFor = :selectedDate AND mealNumber > :mealNumber ORDER BY mealNumber")
    suspend fun getMealsWithHigherNumbers(selectedDate: String, mealNumber: Int): List<Meal>

}

