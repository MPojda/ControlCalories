package com.example.controlcalories.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.R
import com.example.controlcalories.ui.theme.defaultButtonColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityInputScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val selectedProduct = viewModel.selectedProduct.collectAsState().value
    val weightText = remember { mutableStateOf("") }
    val isWeightValid = remember(weightText.value) {
        weightText.value.toFloatOrNull()?.let { it > 0 } ?: false
    }
    val currentMealId = viewModel.currentMealId.collectAsState().value

    LaunchedEffect(weightText.value) {
        Log.d("Debug", "Weight text: ${weightText.value}")
        Log.d("Debug", "Weight is valid: $isWeightValid")
    }

    LaunchedEffect(currentMealId) {
        Log.d("Debug", "Current meal ID: $currentMealId")
    }

    val addingProduct = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = selectedProduct?.name ?: "Wybierz produkt...",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp, color = Color.White),
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = defaultButtonColor, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = weightText.value,
                onValueChange = { weightText.value = it },
                label = { Text("Waga (w gramach)", color = Color.White) },
                isError = !isWeightValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.White,
                    cursorColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .background(color = defaultButtonColor, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (addingProduct.value) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Button(
                    onClick = {
                        if (isWeightValid && selectedProduct != null && currentMealId != null) {
                            addingProduct.value = true
                            viewModel.addUserProduct(
                                weight = weightText.value.toFloat(),
                                categoryId = selectedProduct.categoryId,
                                mealId = currentMealId,
                                onProductAdded = {
                                    navController.popBackStack()
                                    addingProduct.value = false
                                }
                            )
                        } else {
                            Log.d("Debug", "Cannot add product: Check product selection, weight, or meal ID.")
                        }
                    },
                    enabled = isWeightValid && selectedProduct != null && currentMealId != null,
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = defaultButtonColor)
                ) {
                    Text("Dodaj produkt do posiłku", color = Color.White)
                }
            }
        }
    }
}





