package com.example.controlcalories.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.ui.theme.Typography
import com.example.controlcalories.ui.theme.defaultButtonColor
import com.example.controlcalories.ui.theme.defaultErrorColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiScreen(
    viewModel: MainViewModel, navController: NavHostController
) {
    val gender by viewModel.gender.collectAsState()
    val dayOfBirth by viewModel.dayOfBirth.collectAsState()
    val monthOfBirth by viewModel.monthOfBirth.collectAsState()
    val yearOfBirth by viewModel.yearOfBirth.collectAsState()
    val height by viewModel.height.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val showErrorAlert by viewModel.showErrorAlert.collectAsState()

    val isDayValid by viewModel.isDayValid.collectAsState()
    val isMonthValid by viewModel.isMonthValid.collectAsState()
    val isYearValid by viewModel.isYearValid.collectAsState()
    val isWeightValid by viewModel.isWeightValid.collectAsState()
    val isHeightValid by viewModel.isHeightValid.collectAsState()
    val showResultDialog by viewModel.showResultDialog.collectAsState()

    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {

        Text(
            "Płeć:", style = Typography.labelLarge, modifier = Modifier.padding(start = 10.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
            Checkbox(
                checked = gender == "Kobieta",
                onCheckedChange = {
                    if (it) viewModel.updateGender("Kobieta")
                },
                colors = CheckboxDefaults.colors(checkmarkColor = Color.White),
                modifier = Modifier.padding(end = 16.dp)
            )
            Text("Kobieta", style = Typography.labelLarge)

            Checkbox(
                checked = gender == "Mężczyzna",
                onCheckedChange = {
                    if (it) viewModel.updateGender("Mężczyzna")
                    else viewModel.updateGender("")
                },
                colors = CheckboxDefaults.colors(checkmarkColor = Color.White),
                modifier = Modifier.padding(start = 16.dp)
            )
            Text("Mężczyzna", style = Typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(18.dp))
        Text(
            "Data urodzenia:",
            style = Typography.labelLarge,
            modifier = Modifier.padding(start = 10.dp),
        )
        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                value = dayOfBirth,
                onValueChange = { newText ->
                    viewModel.updateDayOfBirth(newText)
                },
                isError = !isDayValid,
                label = { Text("Dzień") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isDayValid) Color(0xFF4CAF50) else Color.Red,
                    unfocusedBorderColor = if (isDayValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                value = monthOfBirth,
                onValueChange = { newText ->
                    viewModel.updateMonthOfBirth(newText)
                },
                isError = !isMonthValid,
                label = { Text("Miesiąc") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isMonthValid) Color(0xFF4CAF50) else Color.Red,
                    unfocusedBorderColor = if (isMonthValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                value = yearOfBirth,
                onValueChange = { newText ->
                    viewModel.updateYearOfBirth(newText)
                },
                isError = !isYearValid,
                label = { Text("Rok") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isYearValid) Color(0xFF4CAF50) else Color.Red,
                    unfocusedBorderColor = if (isYearValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            "Dane do obliczenia BMI:",
            style = Typography.labelLarge,
            modifier = Modifier.padding(start = 10.dp),
        )
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {


            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                value = height,
                onValueChange = { newText ->
                    viewModel.updateHeight(newText)
                },
                isError = !isHeightValid,
                label = { Text("Wzrost w cm") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isHeightValid) Color(0xFF4CAF50) else Color.Red,
                    unfocusedBorderColor = if (isHeightValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                value = weight,
                onValueChange = { newText ->
                    viewModel.updateWeight(newText)
                },
                isError = !isWeightValid,
                label = { Text("Waga w kg") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isWeightValid) Color(0xFF4CAF50) else Color.Red,
                    unfocusedBorderColor = if (isWeightValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {

            Button(
                onClick = {
                    viewModel.calculateAndSaveBMI()
                    if (showErrorAlert) {
                        coroutineScope.launch {
                            delay(3000)  // Czas po jakim alert o błędzie zniknie
                            viewModel.showErrorAlert.value = false
                        }
                    }
                },
                enabled = true,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (showErrorAlert) defaultErrorColor else defaultButtonColor),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 10.dp)
            ) {
                Text(text = "Oblicz BMI")
            }

            if (showResultDialog) {
                AlertDialog(
                    onDismissRequest = {

                    },
                    title = { Text("Wynik BMI") },
                    text = {
                        Column {
                            viewModel.bmiResult.value?.let { bmi ->
                                Text(
                                    "Twoje BMI to ${"%.2f".format(bmi)}",
                                    style = Typography.labelMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            viewModel.bmiCategory.value?.let { category ->
                                Text(
                                    "Kategoria: $category",
                                    style = Typography.labelMedium
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.showResultDialog.value = false
                                viewModel.setUserNotFirstTime()
                                navController.navigate("mainMenu") {
                                    popUpTo("start") { inclusive = true }
                                }
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}