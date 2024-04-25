package com.example.controlcalories.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.data.model.dto.UserProduct
import com.example.controlcalories.ui.theme.defaultButtonColor

@Composable
fun MealDetailScreen(
    viewModel: MainViewModel,
    mealId: Int,
    navController: NavHostController
) {
    val productsInMeal by viewModel.getProductsFromMeal(mealId).collectAsState(initial = listOf())

    Column {
        productsInMeal.forEach { userProduct ->
            ProductEdit(
                product = userProduct,
                onEdit = { updatedWeight -> viewModel.updateProductWeight(userProduct, updatedWeight) },
                onDelete = { viewModel.deleteProductFromMeal(userProduct) }
            )
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultButtonColor)
        ) {
            Text("Powrót")
        }
    }
}

@Composable
fun ProductEdit(
    product: UserProduct,
    onEdit: (Float) -> Unit,
    onDelete: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(text = product.name, style = MaterialTheme.typography.labelMedium)
            Text(text = "Kalorie: ${product.calories}, Białko: ${product.protein}")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Usuń produkt")
        }
        TextField(
            value = product.weight.toString(),
            onValueChange = { newValue -> onEdit(newValue.toFloatOrNull() ?: product.weight) },
            label = { Text("Gramatura") }
        )
    }
}







