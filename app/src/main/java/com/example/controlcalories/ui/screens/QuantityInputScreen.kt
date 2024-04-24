package com.example.controlcalories.ui.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
fun QuantityInputScreen(viewModel: MainViewModel, navController: NavHostController) {
    val selectedProduct = viewModel.selectedProduct.collectAsState().value
    val weightText = remember { mutableStateOf("") }
    val isWeightValid = remember(weightText.value) {
        weightText.value.toFloatOrNull()?.let { it > 0 } ?: false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp, 200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${selectedProduct?.name}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 22.sp,
                    color = Color.White
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = defaultButtonColor, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(color = defaultButtonColor, shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp)),
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
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (isWeightValid) {
                            viewModel.addUserProduct(
                                weightText.value.toFloat(),
                                selectedProduct?.categoryId ?: 0
                            )
                            navController.popBackStack()
                        }
                    },
                    enabled = isWeightValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = defaultButtonColor,
                        disabledContainerColor = defaultButtonColor
                    )
                ) {
                    Text(
                        text = "Dodaj produkt do posiłku",
                        color = Color.White
                    )
                }
            }
        }
    }







