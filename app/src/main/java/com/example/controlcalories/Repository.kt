package com.example.controlcalories

import android.content.Context
import android.util.Log
import com.example.controlcalories.data.model.dto.ProductDao
import com.example.controlcalories.data.model.dto.ProductDatabase
import com.example.controlcalories.data.model.dto.ProductLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Repository(context: Context) {

    private val productLoader = ProductLoader(context)

    private val database = ProductDatabase.getInstance(context)

    private val productDao = database.productDao()


    init {
        CoroutineScope(Dispatchers.IO).launch {
        if (productDao.getTableSize() <= 0) {

                val productList = productLoader.loadProductsFromCsv(fileName = "Produkty.csv")
                Log.d(":dupa", productList.toString())

                productDao.insertAll(productList)
            }

        }

    }

}