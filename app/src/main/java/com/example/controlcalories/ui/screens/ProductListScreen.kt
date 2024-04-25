package com.example.controlcalories.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.data.model.dto.Product
import com.example.controlcalories.ui.theme.defaultButtonColor

@Composable
fun ProductListScreen(
    categoryId: Int,
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val products by viewModel.getProductsByCategory(categoryId).collectAsState(initial = listOf())

    LazyColumn {
        items(products, key = { product -> product.id }) { product ->
            ProductItem(product = product, onSelectProduct = {
                viewModel.selectProduct(product)
                navController.navigate("QuantityInput")
            })
        }
    }
}

@Composable
fun ProductItem(product: Product, onSelectProduct: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable(onClick = onSelectProduct),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = defaultButtonColor)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
        }
    }
}