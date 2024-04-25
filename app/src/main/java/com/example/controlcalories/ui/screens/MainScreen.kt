package com.example.controlcalories.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.R
import com.example.controlcalories.data.model.dto.Product
import com.example.controlcalories.data.model.dto.ProductCategory
import com.example.controlcalories.ui.default_components.WeekdayButton
import com.example.controlcalories.ui.theme.Typography
import com.example.controlcalories.ui.theme.defaultButtonColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val meals by viewModel.meals.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadTodayMeals()
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
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
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                WeekdayButton(text = "Pon", onClick = { /* Handle button click */ }, viewModel = viewModel)
                WeekdayButton(text = "Wt", onClick = { /* Handle button click */ }, viewModel = viewModel)
                WeekdayButton(text = "Śr", onClick = { /* Handle button click */ }, viewModel = viewModel)
                WeekdayButton(text = "Czw", onClick = { /* Handle button click */ }, viewModel = viewModel)
                WeekdayButton(text = "Pt", onClick = { /* Handle button click */ }, viewModel = viewModel)
                WeekdayButton(text = "Sob", onClick = { /* Handle button click */ }, viewModel = viewModel)
                WeekdayButton(text = "Ndz", onClick = { /* Handle button click */ }, viewModel = viewModel)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 74.dp)
            ) {
                Text(
                    text = "B",
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "T",
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "W",
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(meals) { meal ->
            MealItem(meal = meal, navController = navController)
        }

        item {
            Button(
                onClick = { viewModel.addMeal() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = buttonColors(containerColor = defaultButtonColor)
            ) {
                Text("Dodaj posiłek", color = Color.White)
            }
        }
    }
}

@Composable
fun MealItem(
    meal: String,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = meal, style = Typography.labelMedium)
        Row {
            IconButton(
                onClick = { navController.navigate("categories") },
                modifier = Modifier
                    .size(48.dp)
                    .background(defaultButtonColor, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Dodaj produkt",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { navController.navigate("mealDetails") },
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










