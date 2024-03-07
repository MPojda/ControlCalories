package com.example.controlcalories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.controlcalories.data.model.dto.ProductDao
import com.example.controlcalories.ui.screens.BmiScreen
import com.example.controlcalories.ui.screens.MainScreen
import com.example.controlcalories.ui.screens.StartScreen
import com.example.controlcalories.ui.theme.ControlCaloriesTheme

class MainActivity : ComponentActivity() {
    private lateinit var productDao: ProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContent {
//            val navController = rememberNavController()
//            ControlCaloriesTheme {
//                NavigationHost(navController = navController, productDao = productDao)
//            }
//        }
        Repository(context = applicationContext)
    }
}

@Composable
fun NavigationHost(navController: NavHostController, productDao: ProductDao) {
    NavHost(navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController = navController)
        }
        composable("bmi") {
            BmiScreen(navController = navController)
        }
        composable("menu") {
            MainScreen(navController = navController, productDao = productDao)
        }
    }
}