package com.example.controlcalories.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.data.model.dto.ProductCategory

@Composable
fun CategoriesScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CategoryList(viewModel, navController)
    }
}

@Composable
fun CategoryList(viewModel: MainViewModel, navController: NavHostController) {
    val categories = viewModel.categories.collectAsState().value

    LazyColumn {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onSelectCategory = {
                    viewModel.selectCategory(category.id)
                    navController.navigate("ProductList/${category.id}")  // Załóżmy, że masz taki route w NavController
                }
            )
        }
    }
}

@Composable
fun CategoryItem(category: ProductCategory, onSelectCategory: () -> Unit) {
    Text(
        text = category.name,
        modifier = Modifier.clickable(onClick = onSelectCategory)
    )
}