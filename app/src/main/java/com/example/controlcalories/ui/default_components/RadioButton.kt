package com.example.controlcalories.ui.default_components

import android.widget.RadioButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RadioButton(
    text: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val isSelected = text == selectedOption

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = { onOptionSelected(text) }
            )
            .padding(8.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else Transparent
            )
            .clip(MaterialTheme.shapes.small)
    ) {
        androidx.compose.material3.RadioButton(
            selected = isSelected,
            onClick = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun RadioButtonPreview() {
    var selectedOption by remember { mutableStateOf("Kobieta") }

    RadioButton(
        text = "Płeć",
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it }
    )
}