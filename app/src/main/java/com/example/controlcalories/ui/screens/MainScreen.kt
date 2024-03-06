package com.example.controlcalories.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.controlcalories.R
import com.example.controlcalories.data.model.dto.ProductDao
import com.example.controlcalories.data.model.dto.populateDatabase
import com.example.controlcalories.ui.default_components.AddButton
import com.example.controlcalories.ui.default_components.EditButton
import com.example.controlcalories.ui.default_components.WeekdayButton
import com.example.controlcalories.ui.theme.defaultButtonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream


@Composable
fun AddButtonWithMeal(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Add Meal"
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AddButton(onClick = onClick, contentDescription = contentDescription)

    }
}

@Composable
fun AddMealWithEdit(
    mealNumber: Int,
    onClickEdit: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Posiłek $mealNumber",
            color = Color.Black,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            EditButton(onClick = onClickEdit)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    productDao: ProductDao,
) {
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
            }
        }
        onDispose {  }
    }



    var mealsCount by remember { mutableStateOf(3) } // Ilość posiłków do wyświetlenia
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Opis zdjęcia",
                modifier = Modifier.size(150.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            WeekdayButton(text = "Pon", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Wt", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Śr", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Czw", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Pt", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Sob", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Ndz", onClick = { /* Handle button click */ })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 74.dp)

        ) {
            Text(
                text = "B",
                color = defaultButtonColor,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "T",
                color = defaultButtonColor,
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "W",
                color = defaultButtonColor,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        repeat(mealsCount) { mealIndex ->
            AddMealWithEdit(
                mealNumber = mealIndex + 1,
                onClickEdit = { /* Obsługa kliknięcia przycisku Edytuj */ }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        AddButtonWithMeal(onClick = {
            mealsCount++
        })
    }
}

