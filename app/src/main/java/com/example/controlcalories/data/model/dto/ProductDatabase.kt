package com.example.controlcalories.data.model.dto

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Product::class], version = 2)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "app_database"
                )

                    .build()
                INSTANCE = instance
                instance
            }
        }
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Stwórz nową tabelę zgodnie z nową strukturą
                database.execSQL("CREATE TABLE IF NOT EXISTS product_table_new (uid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, calories INTEGER, protein REAL, fat REAL, carbohydrates INTEGER, sugar REAL, fiber REAL)")
                // Skopiuj dane z istniejącej tabeli do nowej tabeli
                database.execSQL("INSERT INTO product_table_new SELECT uid, name, calories, protein, fat, CAST(carbohydrates AS INTEGER), sugar, fiber FROM product_table")
                // Usuń starą tabelę
                database.execSQL("DROP TABLE IF EXISTS product_table")
                // Zmień nazwę nowej tabeli na poprzednią nazwę tabeli
                database.execSQL("ALTER TABLE product_table_new RENAME TO product_table")
            }
        }

    }
}