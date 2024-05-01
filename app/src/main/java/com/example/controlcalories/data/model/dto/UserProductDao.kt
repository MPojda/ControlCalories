package com.example.controlcalories.data.model.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProduct: UserProduct)

    @Update
    suspend fun update(userProduct: UserProduct)

    @Delete
    suspend fun delete(userProduct: UserProduct)

    @Query("DELETE FROM user_products WHERE mealId = :mealId")
    suspend fun deleteProductsWithMeal(mealId: Int)

    @Query("SELECT * FROM user_products ORDER BY dateFor DESC")
    fun getAllUserProducts(): Flow<List<UserProduct>>

    @Query("SELECT * FROM user_products WHERE mealId = :mealId ORDER BY dateFor DESC")
    fun getProductsFromMeal(mealId: Int): Flow<List<UserProduct>>

    @Query("SELECT * FROM user_products WHERE mealId = :mealId AND dateFor = :dateFor ORDER BY dateFor DESC")
    fun getProductsForMealOnDay(mealId: Int, dateFor: String): Flow<List<UserProduct>>

    @Query("SELECT SUM(calories) AS totalCalories, SUM(protein) AS totalProtein, SUM(fat) AS totalFat, SUM(carbohydrates) AS totalCarbohydrates FROM user_products WHERE dateFor = :dateFor")
    fun getTotalsForDay(dateFor: String): Flow<TotalsForDay>
}