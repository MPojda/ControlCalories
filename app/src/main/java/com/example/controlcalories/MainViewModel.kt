package com.example.controlcalories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.controlcalories.data.model.dto.Product
import kotlinx.coroutines.flow.Flow

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repository(app.applicationContext)

}