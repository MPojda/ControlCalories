package com.example.controlcalories.data.model.domain

import androidx.compose.ui.text.font.FontWeight

data class UserData (
    val gender: String,
    val age: Int,
    val weight: Double,
    val targetWeight: Double,
    val height: Double,
)