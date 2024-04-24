package com.example.controlcalories.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.data.model.dto.ProductCategory
import com.example.controlcalories.ui.theme.defaultButtonColor

@Composable
fun CategoriesScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                    navController.navigate("ProductList/${category.id}")
                }
            )
        }
    }
}

@Composable
fun CategoryItem(category: ProductCategory, onSelectCategory: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onSelectCategory),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = defaultButtonColor)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    color = Color.White
                )
            )
        }
    }
}