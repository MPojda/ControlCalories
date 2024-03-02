package com.example.controlcalories.data.model.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Insert
    fun insertAll(products: List<Product>)

    @Query("SELECT * FROM product")
    fun getAllProducts(): List<Product>

    @Query("SELECT * FROM product WHERE id = :productId")
    fun getProductById(productId: String): Product?

    @Delete
    fun deleteProduct(product: Product)

    @Query("DELETE FROM product")
    fun deleteAllProducts()

    @Update
    fun updateProduct(product: Product)
}