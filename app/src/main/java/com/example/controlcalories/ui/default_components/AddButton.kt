package com.example.controlcalories.ui.default_components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controlcalories.ui.theme.defaultButtonColor

@Composable
fun AddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Add Meal"
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.padding(end = 6.dp),
        colors = ButtonDefaults.buttonColors(defaultButtonColor)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = contentDescription,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(text = "posiłek", modifier = Modifier.padding(start = 4.dp))
    }
}

