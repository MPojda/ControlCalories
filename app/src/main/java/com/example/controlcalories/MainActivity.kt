package com.example.controlcalories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.controlcalories.data.model.dto.ProductDao
import com.example.controlcalories.data.model.dto.ProductDatabase
import com.example.controlcalories.ui.screens.CategoriesScreen
import com.example.controlcalories.ui.screens.BmiScreen
import com.example.controlcalories.ui.screens.MainScreen
import com.example.controlcalories.ui.screens.MealDetailScreen
import com.example.controlcalories.ui.screens.ProductListScreen
import com.example.controlcalories.ui.screens.QuantityInputScreen
import com.example.controlcalories.ui.screens.StartScreen
import com.example.controlcalories.ui.theme.ControlCaloriesTheme

class MainActivity : ComponentActivity() {
    private lateinit var productDao: ProductDao
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productDatabase = ProductDatabase.getInstance(applicationContext)
        productDao = productDatabase.productDao()

        setContent {
            val navController = rememberNavController()
            ControlCaloriesTheme {
                NavigationHost(navController = navController, viewModel = viewModel)
            }
        }
        Repository(context = applicationContext)
    }
}

@Composable
fun NavigationHost(navController: NavHostController, viewModel: MainViewModel) {
    val startDestination = if (viewModel.isFirstTimeUser()) "start" else "mainMenu"

    NavHost(navController, startDestination = startDestination) {
        composable("start") {
            StartScreen(
                modifier = Modifier,
                navController = navController,
                onContinueClicked = {
                    viewModel.setUserNotFirstTime()
                    navController.navigate("bmi") {
                        popUpTo("start") { inclusive = true }
                    }
                }
            )
        }

        composable("bmi") {
            BmiScreen(viewModel = viewModel, navController = navController)
        }

        composable("mainMenu") {
            MainScreen(viewModel = viewModel, navController = navController)
        }


        composable("categories") {
            CategoriesScreen(viewModel = viewModel, navController = navController)
        }

        composable("productList/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: throw IllegalArgumentException("Category ID is required")
            ProductListScreen(categoryId = categoryId, viewModel = viewModel, navController = navController)
        }


        composable("quantityInput") {
            QuantityInputScreen(viewModel = viewModel, navController = navController)
        }
        composable("mealDetails") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")?.toIntOrNull() ?: 0
            MealDetailScreen(viewModel = viewModel, mealId = mealId, navController = navController)
        }
    }
}