package com.example.controlcalories.data.repositories

import com.example.controlcalories.data.model.domain.BmiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BmiRepository {
    private val _bmiState = MutableStateFlow<BmiState>(BmiState.NotFilled)

    private fun updateBmiState(BmiState: BmiState){
        _bmiState.update { BmiState }
    }
    fun getBmiState() = _bmiState.asStateFlow()
}


