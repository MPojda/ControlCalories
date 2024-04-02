package com.example.controlcalories.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.R
import com.example.controlcalories.data.model.dto.ProductCategory
import com.example.controlcalories.ui.default_components.WeekdayButton
import com.example.controlcalories.ui.theme.defaultButtonColor



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val categories by viewModel.categories.collectAsState()
    val showCategories = remember { mutableStateOf(false) }
    val meals by viewModel.meals.collectAsState()
    val expandedMeals by viewModel.expandedMeals.collectAsState(initial = mapOf<Int, Boolean>())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Przyciski dni tygodnia
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
        Spacer(modifier = Modifier.height(16.dp))

        // Przyciski skrótów do makroelementów
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

        LazyColumn {
            itemsIndexed(meals) { index, meal ->
                MealItem(meal, index, viewModel, expandedMeals.getOrDefault(index, false))
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Button(
                    onClick = { viewModel.addMeal() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Dodaj kolejny posiłek")
                }
            }
        }
    }
}

@Composable
fun MealItem(meal: String, mealId: Int, viewModel: MainViewModel, isExpanded: Boolean) {
    val categories by viewModel.categories.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.toggleMealExpansion(mealId) }
        ) {
            Text(text = meal, style = MaterialTheme.typography.titleMedium)
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                contentDescription = if (isExpanded) "Zwiń" else "Rozwiń"
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            categories.forEach { category ->
                ExpandableCategory(category, viewModel, remember { mutableStateOf(false) })
            }
        }
    }
}

@Composable
fun ExpandableCategory(
    category: ProductCategory,
    viewModel: MainViewModel,
    showCategories: MutableState<Boolean>
) {
    var isExpanded by remember { mutableStateOf(false) }
    val products by viewModel.products.collectAsState()

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { isExpanded = !isExpanded }
        .padding(8.dp)) {
        Text(text = category.name, style = MaterialTheme.typography.titleSmall)

        AnimatedVisibility(visible = isExpanded) {
            Column {
                products.filter { it.uid in category.startId..category.endId }
                    .forEach { product ->
                        Text(
                            text = product.name,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
            }
        }
    }
}

@Composable
fun WeekdayButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}
