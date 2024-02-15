package com.example.controlcalories.data.model.domain

sealed class FirstOpenEvent {
    data object NotOpened : FirstOpenEvent()
    data object Opened : FirstOpenEvent()

}