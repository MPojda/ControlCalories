package com.example.controlcalories.data.model.domain

sealed class BmiState {
    data object NotFilled : BmiState()
    data object Filled : BmiState()
    data object BmiCalculated : BmiState()
    data object Next : BmiState()

    data class Filling(
        val currentData: UserData,
    )
}