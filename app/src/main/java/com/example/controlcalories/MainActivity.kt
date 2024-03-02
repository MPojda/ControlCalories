package com.example.controlcalories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.controlcalories.data.model.dto.DatabaseProvider
import com.example.controlcalories.data.model.dto.ProductDao
import com.example.controlcalories.ui.screens.BmiScreen
import com.example.controlcalories.ui.screens.MainScreen
import com.example.controlcalories.ui.screens.StartScreen
import com.example.controlcalories.ui.theme.ControlCaloriesTheme
import java.io.InputStream


class MainActivity : ComponentActivity() {
    private lateinit var productDao: ProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseProvider.initialize(this)
        productDao = DatabaseProvider.provideProductDao()

        setContent {
            val navController = rememberNavController()
            ControlCaloriesTheme {
                NavigationHost(navController = navController, inputStream = inputStream, productDao = productDao)
            }
        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController, inputStream: InputStream, productDao: ProductDao) {
    NavHost(navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController = navController)
        }
        composable("bmi") {
            BmiScreen(navController = navController)
        }
        composable("menu") {
            MainScreen(navController = navController, inputStream = inputStream, productDao = productDao)
        }
    }
}

@Composable
fun StartScreen(navController: NavHostController) {
    com.example.controlcalories.StartScreen(navController = navController)
}