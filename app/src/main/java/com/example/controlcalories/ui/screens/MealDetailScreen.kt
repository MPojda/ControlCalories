package com.example.controlcalories.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.data.model.dto.UserProduct
import com.example.controlcalories.ui.theme.defaultButtonColor
import java.time.format.TextStyle

@Composable
fun MealDetailScreen(
    viewModel: MainViewModel,
    mealId: Int,
    navController: NavHostController
) {
    val productsInMeal by viewModel.getProductsFromMeal(mealId).collectAsState(initial = listOf<UserProduct>())

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(productsInMeal) { userProduct ->
            ProductEdit(
                product = userProduct,
                viewModel = viewModel,
                onDelete = { viewModel.deleteProductFromMeal(userProduct) }
            )
        }
        item {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultButtonColor)
            ) {
                Text("Powrót", color = Color.White)
            }
        }
    }
}

@Composable
fun ProductEdit(
    product: UserProduct,
    viewModel: MainViewModel,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = defaultButtonColor)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Usuń produkt",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Kalorie: ${product.calories}", color = Color.White)
                Text(text = "Białko: ${product.protein} g", color = Color.White)
                Text(text = "Tłuszcz: ${product.fat} g", color = Color.White)
                Text(text = "Węglowodany: ${product.carbohydrates} g", color = Color.White)
                Text(text = "Cukier: ${product.sugar} g", color = Color.White)
                Text(text = "Błonnik: ${product.fiber} g", color = Color.White)

            }
            OutlinedTextField(
                value = product.weight.toString(),
                onValueChange = { newValue ->
                    val parsedValue = newValue.toFloatOrNull()
                    if (parsedValue != null && parsedValue >= 0) {
                        viewModel.updateProductInMeal(product, parsedValue)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(120.dp),
                label = { Text("Gramatura", color = Color.White) },
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
    }
}