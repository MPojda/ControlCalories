package com.example.controlcalories.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.data.model.dto.Product

@Composable
fun ProductListScreen(
    categoryId: Int,
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val products = viewModel.products.collectAsState().value.filter { it.categoryId == categoryId }

    LazyColumn {
        items(products) { product ->
            ProductItem(product = product, onSelectProduct = {
                viewModel.selectProduct(product)
                navController.navigate("QuantityInput")  // Przenosi do wprowadzania wagi
            })
        }
    }
}

@Composable
fun ProductItem(product: Product, onSelectProduct: () -> Unit) {
    Text(
        text = product.name,
        modifier = Modifier.clickable(onClick = onSelectProduct)
    )
}