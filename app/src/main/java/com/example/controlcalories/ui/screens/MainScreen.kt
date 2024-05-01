package com.example.controlcalories.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.R
import com.example.controlcalories.Repository
import com.example.controlcalories.data.model.dto.TotalsForDay
import com.example.controlcalories.ui.default_components.WeekdayButton
import com.example.controlcalories.ui.theme.Typography
import com.example.controlcalories.ui.theme.defaultButtonColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val meals by viewModel.meals.collectAsState()
    val totalsForDay by viewModel.totalsForDay.collectAsState(initial = TotalsForDay(0.0, 0.0, 0.0, 0.0))
    LaunchedEffect(Unit) {
        Log.d("MainScreen", "LaunchedEffect triggered")
        viewModel.loadTodayMeals()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        bottomBar = {
            Button(
                onClick = { viewModel.addMeal() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = defaultButtonColor)
            ) {
                Text("Dodaj posiłek", color = Color.White)

            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(150.dp)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 4.dp)
                            .clip(CircleShape)
                            .background(defaultButtonColor)
                            .size(48.dp)
                    ) {
                        IconButton(
                            onClick = { navController.navigate("bmi") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    listOf("Pon", "Wt", "Śr", "Czw", "Pt", "Sob", "Ndz").forEach { day ->
                        WeekdayButton(
                            text = day,
                            onClick = {
                                val mealId = viewModel.getMealIdForDay(day)
                                if (mealId != null) {
                                    navController.navigate("mealDetails/$mealId")
                                }
                            },
                            viewModel = viewModel
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                meals.forEach { meal ->
                    MealItem(
                        mealId = meal.first,
                        mealName = meal.second,
                        navController = navController,
                        viewModel = viewModel)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Kalorie: ${"%.0f".format(totalsForDay.totalCalories)}\n" +
                            "Białka: ${"%.1f".format(totalsForDay.totalProtein)}g\n" +
                            "Tłuszcze: ${"%.1f".format(totalsForDay.totalFat)}g\n" +
                            "Węglowodany: ${"%.1f".format(totalsForDay.totalCarbohydrates)}g",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun MealItem(
    mealId: Int,
    mealName: String,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = mealName, style = Typography.labelMedium)
        Row {
            IconButton(
                onClick = {
                    viewModel.selectMeal(mealId)
                    navController.navigate("categories") },
                modifier = Modifier
                    .size(48.dp)
                    .background(defaultButtonColor, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Dodaj produkt do posiłku",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { viewModel.deleteMeal(mealId) },
                modifier = Modifier
                    .size(48.dp)
                    .background(defaultButtonColor, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Usuń posiłek",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { navController.navigate("mealDetails/$mealId") },
                modifier = Modifier
                    .size(48.dp)
                    .background(defaultButtonColor, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edytuj posiłek",
                    tint = Color.White
                )
            }
        }
    }
}










