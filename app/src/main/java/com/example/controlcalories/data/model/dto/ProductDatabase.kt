package com.example.controlcalories.data.model.dto

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Product::class, UserProduct::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userProductDao(): UserProductDao // Dodaj to

    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase {
            synchronized(this) {
                return INSTANCE ?: createDatabase(context).also { INSTANCE = it }
            }
        }

        private fun createDatabase(context: Context): ProductDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ProductDatabase::class.java,
                "product_database"
            )
                .fallbackToDestructiveMigration() // Dodaj to, jeśli chcesz uniknąć błędów migracji przy zmianie schematu
                .build()
        }
    }
}