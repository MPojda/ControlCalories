package com.example.controlcalories

import android.content.Context
import com.example.controlcalories.data.model.dto.ProductDao
import com.example.controlcalories.data.model.dto.ProductLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Repository(
    private val productDao: ProductDao,
    context: Context
) {
    private val productLoader = ProductLoader(context)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (productDao.getTableSize() <= 0) {
                val productList = productLoader.loadProductsFromCsv(fileName = "Produkty.csv")
                productDao.insertAll(productList)
            }
        }
    }

}