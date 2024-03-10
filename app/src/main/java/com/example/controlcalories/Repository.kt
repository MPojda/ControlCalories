package com.example.controlcalories

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.controlcalories.data.model.dto.ProductDao
import com.example.controlcalories.data.model.dto.ProductDatabase
import com.example.controlcalories.data.model.dto.ProductLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Repository(context: Context) {

    private val productLoader = ProductLoader(context)


    private val database = Room.databaseBuilder(
        context,
        ProductDatabase::class.java,
        "product_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val productDao = database.productDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (productDao.getTableSize() <= 0) {
                val productList = productLoader.loadProductsFromCsv(fileName = "Produkty.csv")
                Log.d(":kupa", productList[25].toString())
                Log.d(":dupa", productList.toString())
                productDao.insertAll(productList)
            }
        }
    }
}