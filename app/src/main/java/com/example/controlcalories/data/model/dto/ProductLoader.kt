package com.example.controlcalories.data.model.dto

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class ProductLoader(private val context: Context) {

    fun loadProductsFromCsv(fileName: String): List<Product> {
        val productList = mutableListOf<Product>()
        try {
            val inputStream = context.assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.useLines { lines ->
                lines.forEachIndexed { index, line ->
                    if (index > 0) {
                        val tokens = line.split(";")
                        if (tokens.size >= 9) {
                            val product = Product(
                                uid = tokens[0].toInt(),
                                name = tokens[1],
                                calories = tokens[2].toIntOrNull() ?: 0,
                                protein = tokens[3].replace(',', '.').toFloatOrNull() ?: 0f,
                                fat = tokens[4].replace(',', '.').toFloatOrNull() ?: 0f,
                                carbohydrates = tokens[5].replace(',', '.').toFloatOrNull() ?: 0f,
                                sugar = tokens[6].replace(',', '.').toFloatOrNull() ?: 0f,
                                fiber = tokens[7].replace(',', '.').toFloatOrNull() ?: 0f,
                                categoryId = tokens[8].toInt()
                            )
                            productList.add(product)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return productList
    }
}