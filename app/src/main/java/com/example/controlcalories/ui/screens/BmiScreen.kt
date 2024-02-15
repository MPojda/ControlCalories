package com.example.controlcalories.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import kotlin.math.round
import java.time.LocalDate
import java.time.Period
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.controlcalories.ui.theme.Typography
import com.example.controlcalories.ui.theme.defaultButtonColor
import com.example.controlcalories.ui.theme.defaultErrorColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiScreen(
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    selectedOption: String = "",
    enabledEditing: Boolean = true,
    onValueChange: (String) -> Unit = {}
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
                    Text(text = "Dzień")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isDayValid) Color(0xFF4CAF50) else Color.Black,
                    unfocusedBorderColor = if (isDayValid) Color(0xFF4CAF50) else Color.Black,
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
                    unfocusedBorderColor = if (isMonthValid) Color(0xFF4CAF50) else Color.Black,
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
                    unfocusedBorderColor = if (isYearValid) Color(0xFF4CAF50) else Color.Black,
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
                    unfocusedBorderColor = if (isHeightValid) Color(0xFF4CAF50) else Color.Black,
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
                    unfocusedBorderColor = if (isWeightValid) Color(0xFF4CAF50) else Color.Black,
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
        }
    }
}

fun calculateAge(dateOfBirth: LocalDate): Int {
    val currentDate = LocalDate.now()
    val age = Period.between(dateOfBirth, currentDate).years
    return age
}

fun getBMICategory(bmi: Float, age: Int): String {
    return when {
        age < 18 -> {
            when {
                bmi < 15.0 -> "Wygłodzenie"
                bmi in 15.0..15.9 -> "Wychudzenie"
                bmi in 16.0..17.4 -> "Niedowaga"
                bmi in 17.5..23.9 -> "Waga prawidłowa"
                bmi in 24.0..28.9 -> "Nadwaga"
                bmi in 29.0..33.9 -> "Otyłość 1 stopnia"
                bmi in 34.0..38.9 -> "Otyłość 2 stopnia"
                else -> "Otyłość 3 stopnia"
            }
        }
        age >= 65 -> {
            when {
                bmi < 19.0 -> "Wygłodzenie"
                bmi in 19.0..19.9 -> "Wychudzenie"
                bmi in 20.0..21.4 -> "Niedowaga"
                bmi in 21.5..27.9 -> "Waga prawidłowa"
                bmi in 28.0..32.9 -> "Nadwaga"
                bmi in 33.0..37.9 -> "Otyłość 1 stopnia"
                bmi in 38.0..42.9 -> "Otyłość 2 stopnia"
                else -> "Otyłość 3 stopnia"
            }
        }
        else -> {
            when {
                bmi < 16.0 -> "Wygłodzenie"
                bmi in 16.0..16.9 -> "Wychudzenie"
                bmi in 17.0..18.4 -> "Niedowaga"
                bmi in 18.5..24.9 -> "Waga prawidłowa"
                bmi in 25.0..29.9 -> "Nadwaga"
                bmi in 30.0..34.9 -> "Otyłość 1 stopnia"
                bmi in 35.0..39.9 -> "Otyłość 2 stopnia"
                else -> "Otyłość 3 stopnia"
            }
        }
    }
}

fun calculateBMI(height: Int?, weight: Int?, age: Int?): Float {
    if (height == null || weight == null || height <= 0 || weight <= 0) {
        return -1f
    }

    val heightInMeters = height.toFloat() / 100
    var bmi = weight.toFloat() / (heightInMeters * heightInMeters)

    if (age != null) {
        bmi += when {
            age < 18 -> -1.0f
            age >= 65 -> 3.0f
            else -> 0.0f
        }
    }

    return round(bmi * 10) / 10
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BmiScreenPreview() {
    CompositionLocalProvider(LocalContentColor provides Color.Black) {
        BmiScreen(selectedOption = "Kobieta")
    }
}