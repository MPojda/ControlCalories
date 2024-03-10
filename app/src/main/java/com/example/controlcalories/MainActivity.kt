package com.example.controlcalories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.controlcalories.data.model.dto.ProductDao
import com.example.controlcalories.data.model.dto.ProductDatabase
import com.example.controlcalories.ui.screens.BmiScreen
import com.example.controlcalories.ui.screens.MainScreen
import com.example.controlcalories.ui.screens.StartScreen
import com.example.controlcalories.ui.theme.ControlCaloriesTheme
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    private lateinit var productDao: ProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicjalizacja productDao
        val productDatabase = ProductDatabase.getInstance(applicationContext)
        productDao = productDatabase.productDao()

        setContent {
            val navController = rememberNavController()
            ControlCaloriesTheme {
                NavigationHost(navController = navController, productDao = productDao)
            }
        }
        Repository(context = applicationContext)
    }
}

@Composable
fun NavigationHost(navController: NavHostController, productDao: ProductDao) {
    NavHost(navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController = navController, modifier = Modifier)
        }
        composable("bmi") {
            BmiScreen(navController = navController, modifier = Modifier)
        }
        composable("menu") {
            MainScreen(navController = navController,modifier = Modifier, productDao = productDao)
        }
    }
}