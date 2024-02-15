package com.example.controlcalories.ui.default_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.controlcalories.ui.theme.defaultButtonColor
import com.example.controlcalories.ui.theme.defaultColor
import com.example.controlcalories.ui.theme.defaultCorrectColor
import com.example.controlcalories.ui.theme.defaultHistoryCardColor
import com.example.controlcalories.ui.theme.defaultHistoryTextColor
import java.util.*

@Composable
fun WeekdayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isToday = isToday(text)

    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier
            .width(52.dp)
            .height(50.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = if (isToday) defaultHistoryTextColor else defaultButtonColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


private fun isToday(weekday: String): Boolean {
    val calendar = Calendar.getInstance()
    val currentWeekdayIndex = calendar.get(Calendar.DAY_OF_WEEK)

    return when (weekday) {
        "Pon" -> currentWeekdayIndex == Calendar.MONDAY
        "Wt" -> currentWeekdayIndex == Calendar.TUESDAY
        "Śr" -> currentWeekdayIndex == Calendar.WEDNESDAY
        "Czw" -> currentWeekdayIndex == Calendar.THURSDAY
        "Pt" -> currentWeekdayIndex == Calendar.FRIDAY
        "Sob" -> currentWeekdayIndex == Calendar.SATURDAY
        "Ndz" -> currentWeekdayIndex == Calendar.SUNDAY
        else -> false
    }
}