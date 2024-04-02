package com.example.controlcalories.data.model.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    fun insertAll(products: List<Product>)

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM product_table WHERE uid = :productId")
    fun getProductById(productId: Int): Flow<Product?>

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM product_table")
    suspend fun deleteAllProducts()

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT COUNT(*) FROM product_table")
    fun getTableSize() : Int
}