package com.example.controlcalories.data.model.domain

import java.time.LocalDate
import java.time.Period
import kotlin.math.round

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