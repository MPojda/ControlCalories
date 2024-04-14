package com.example.controlcalories.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel

@Composable
fun QuantityInputScreen(viewModel: MainViewModel, navController: NavHostController) {
    val selectedProduct = viewModel.selectedProduct.collectAsState().value
    val weightText = remember { mutableStateOf("") }
    val isWeightValid = remember(weightText.value) {
        weightText.value.toFloatOrNull()?.let { it > 0 } ?: false
    }

    Column {
        Text("Wprowadź wagę dla ${selectedProduct?.name}")
        TextField(
            value = weightText.value,
            onValueChange = { weightText.value = it },
            label = { Text("Waga (w gramach)") },
            isError = !isWeightValid
        )
        Button(
            onClick = {
                if (isWeightValid) {
                    viewModel.addUserProduct(weightText.value.toFloat(), selectedProduct?.categoryId ?: 0)
                    navController.popBackStack()  // Powrót do poprzedniego ekranu
                }
            },
            enabled = isWeightValid
        ) {
            Text("Dodaj produkt do posiłku")
        }
    }
}






