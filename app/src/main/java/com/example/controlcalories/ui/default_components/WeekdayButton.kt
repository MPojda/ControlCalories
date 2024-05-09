package com.example.controlcalories.ui.default_components



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.ui.theme.defaultButtonColor
import com.example.controlcalories.ui.theme.defaultIncorrectColor
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

fun getDayOfWeekAbbreviation(date: LocalDate): String {
    return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).replace(".", "")
}
@Composable
fun WeekdayButton(
    dateFor: String,
    onClick: (String) -> Unit,
    viewModel: MainViewModel,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    val date = LocalDate.parse(dateFor)
    val dayOfWeekAbbreviation = getDayOfWeekAbbreviation(date)
    val dayOfMonth = date.dayOfMonth.toString()

    val buttonSize = 80.dp

    androidx.compose.material3.Button(
        onClick = { viewModel.updateSelectedDate(dateFor) },
        modifier = modifier
            .size(buttonSize)
            .padding(horizontal = 2.dp)
            .padding(bottom = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isToday) defaultIncorrectColor else defaultButtonColor
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = dayOfMonth,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = dayOfWeekAbbreviation,
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}


