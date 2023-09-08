package com.example.controlcalories


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CalculationFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation_form)
        val goalRequirements = intent.getIntExtra("calculation", 0)

    }
}