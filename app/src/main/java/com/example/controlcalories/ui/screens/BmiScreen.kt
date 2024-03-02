package com.example.controlcalories.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import java.time.LocalDate
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.controlcalories.data.model.domain.calculateAge
import com.example.controlcalories.data.model.domain.calculateBMI
import com.example.controlcalories.data.model.domain.getBMICategory
import com.example.controlcalories.ui.theme.Typography
import com.example.controlcalories.ui.theme.defaultButtonColor
import com.example.controlcalories.ui.theme.defaultErrorColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    textColor: Color = Color.Black,
    selectedOption: String = "",
    enabledEditing: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    var gender by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Float?>(null) }
    var age by remember { mutableStateOf(0) }
    var isDayValid by remember { mutableStateOf(day.toIntOrNull() in 1..31) }
    var isMonthValid by remember { mutableStateOf(month.toIntOrNull() in 0..12) }
    var isYearValid by remember { mutableStateOf(year.toIntOrNull() in 1900..2023) }
    var isHeightValid by remember { mutableStateOf(height.toIntOrNull() in 0..999) }
    var isWeightValid by remember { mutableStateOf(weight.toIntOrNull() in 0..999) }
    var showErrorAlert by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {

        Text("Płeć:",
            style = Typography.labelLarge,
            modifier = Modifier.padding(start = 10.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Checkbox(
                checked = gender == "Kobieta",
                onCheckedChange = {
                    gender = if (it) "Kobieta" else ""
                },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = Color.White,
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Black
                ),
                modifier = Modifier.padding(end = 16.dp)


            )
            Text(text = "Kobieta", style = Typography.labelLarge)

            Checkbox(
                checked = gender == "Mężczyzna",
                onCheckedChange = {
                    gender = if (it) "Mężczyzna" else ""
                },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = Color.White,
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Black
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(text = "Mężczyzna", style = Typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(18.dp))
        Text("Data urodzenia:",
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
                value = day,
                onValueChange = { newText ->
                    if (newText.length <= 2) {
                        day = newText
                        isDayValid = newText.toIntOrNull() in 1..31
                    }
                },
                label = {
                    Text(text = "Dzień",)
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isDayValid) Color(0xFF4CAF50) else Color.Black,
                    unfocusedBorderColor = if (isDayValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                value = month,
                onValueChange = { newText ->
                    if (newText.length <= 2) {
                        month = newText
                        isMonthValid = newText.toIntOrNull() in 1..12
                    }
                },
                label = {
                    Text(text = "Miesiąc")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isMonthValid) Color(0xFF4CAF50) else Color.Black,
                    unfocusedBorderColor = if (isMonthValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                value = year,
                onValueChange = { newText ->
                    if (newText.length <= 4) {
                        year = newText
                        isYearValid = newText.toIntOrNull() in 1900..2023
                    }
                },
                label = {
                    Text(text = "Rok")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isYearValid) Color(0xFF4CAF50) else Color.Black,
                    unfocusedBorderColor = if (isYearValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text("Dane do obliczenia BMI:",
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
                    if (newText.length <= 3) {
                        height = newText
                        isHeightValid = newText.toIntOrNull() in 80..300
                    }
                },
                label = {
                    Text(text = "Wzrost w cm")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isHeightValid) Color(0xFF4CAF50) else Color.Black,
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
                    if (newText.length <= 3) {
                        weight = newText
                        isWeightValid = newText.toIntOrNull() in 1..400
                    }
                },
                label = {
                    Text(text = "Waga w kg")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isWeightValid) Color(0xFF4CAF50) else Color.Black,
                    unfocusedBorderColor = if (isWeightValid) Color(0xFF4CAF50) else Color.Red,
                    cursorColor = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {

            Button(
                onClick = {
                    val isValidInput = !(day.isEmpty() || month.isEmpty() || year.isEmpty() || height.isEmpty() || weight.isEmpty() ||
                            !isDayValid || !isMonthValid || !isYearValid || !isHeightValid || !isWeightValid)

                    if (isValidInput) {
                        val dateOfBirth = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
                        age = calculateAge(dateOfBirth)
                        bmiResult = calculateBMI(height.toIntOrNull(), weight.toIntOrNull(), age)
                    } else {
                        showErrorAlert = true
                        coroutineScope.launch {
                            delay(3000)
                            showErrorAlert = false
                        }
                    }
                },
                enabled = true,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp), // Dodajemy odstęp na dole, aby alert nie zakrywał wyników BMI
                colors = ButtonDefaults.buttonColors(containerColor = if (showErrorAlert) defaultErrorColor else defaultButtonColor),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 10.dp)
            ) {
                Text(text = "Oblicz BMI")
            }

            if (showErrorAlert) {
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    color = defaultButtonColor,
                    shape = RoundedCornerShape(10.dp),

                ) {
                    Text(
                        text = "Wypełnij wszystkie pola !",
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            bmiResult?.let { bmi ->
                Text(
                    text = "BMI: $bmi",
                    modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
                )
                val bmiCategory = getBMICategory(bmi, age)
                Text(
                    text = "Interpretacja wyniku: $bmiCategory",
                    modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
                )
            }
            Button(
                onClick = {
                    navController.navigate("menu")
                },
                enabled = bmiResult != null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultButtonColor),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 10.dp)
            ) {
                Text(text = "Zaczynajmy!")
            }
        }
    }
}


