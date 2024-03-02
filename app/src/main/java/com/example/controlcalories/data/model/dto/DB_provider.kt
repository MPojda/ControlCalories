package com.example.controlcalories.data.model.dto

import android.content.Context


object DatabaseProvider {
    private lateinit var appDatabase: AppDatabase

    fun initialize(context: Context) {
        appDatabase = AppDatabase.getInstance(context)
    }

    fun provideProductDao() = appDatabase.productDao()
}