package com.example.controlcalories.data.model.dto

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

suspend fun populateDatabase(context: Context, inputStream: InputStream, productDao: ProductDao) {
    withContext(Dispatchers.IO) {
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            val tokens = line?.split(";")
            val product = Product(
                id = tokens?.get(0) ?: "",
                name = tokens?.get(1) ?: "",
                calories = tokens?.get(2)?.toIntOrNull() ?: 0,
                protein = tokens?.get(3)?.toDoubleOrNull() ?: 0.0,
                fat = tokens?.get(4)?.toDoubleOrNull() ?: 0.0,
                carbohydrates = tokens?.get(5)?.toDoubleOrNull() ?: 0.0,
                sugar = tokens?.get(6)?.toDoubleOrNull() ?: 0.0,
                fiber = tokens?.get(7)?.toDoubleOrNull() ?: 0.0
            )
            productDao.insertAll(listOf(product))
        }
    }
}
