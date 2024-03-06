package com.example.controlcalories.data.local

import android.content.Context
import com.example.controlcalories.data.model.dto.Product
import com.example.controlcalories.data.model.dto.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class ProductLoader(private val context: Context, private val productDao: ProductDao) {

    suspend fun loadProductsFromCsv(fileName: String) {
        withContext(Dispatchers.IO) {
            val inputStream = context.assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val productList = mutableListOf<Product>()

            bufferedReader.useLines { lines ->
                lines.forEach { line ->
                    val tokens = line.split(",")
                    if (tokens.size >= 3) {
                        val product = Product(
                            id = tokens[0],
                            name = tokens[1],
                            calories = tokens[2].toInt(),
                            protein = tokens[3].toInt(),
                            fat = tokens[3].toInt(),
                            carbohydrates = tokens[3].toInt(),
                            sugar = tokens[3].toInt(),
                            fiber = tokens[3].toInt(),
                        )
                        productList.add(product)
                    }
                }
            }

            productDao.insertAll(productList)
        }
    }
}