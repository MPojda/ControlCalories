package com.example.controlcalories


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        val button1: Button = findViewById(R.id.button1)
        button1.setOnClickListener {
            button1.setBackgroundColor(Color.RED)
            startActivity(Intent(this, CalculationFormActivity::class.java).apply { this.putExtra("calculation", -300) })
        }

        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener {
            button2.setBackgroundColor(Color.RED)
            startActivity(Intent(this, CalculationFormActivity::class.java).apply { this.putExtra("calculation", 300) })
        }

        val button3: Button = findViewById(R.id.button3)
        button3.setOnClickListener {
            button3.setBackgroundColor(Color.RED)
            startActivity(Intent(this, CalculationFormActivity::class.java).apply { this.putExtra("calculation", 0) })
        }
    }
}