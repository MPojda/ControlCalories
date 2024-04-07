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

    @Query("SELECT * FROM user_products ORDER BY addedDate DESC")
    fun getAllUserProducts(): Flow<List<UserProduct>>
}