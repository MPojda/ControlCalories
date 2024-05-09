package com.example.controlcalories.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.R
import com.example.controlcalories.data.model.dto.TotalsForDay
import com.example.controlcalories.ui.default_components.WeekdayButton
import com.example.controlcalories.ui.theme.defaultButtonColor


@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController

) {
    val meals by viewModel.meals.collectAsState()
    val totalsForDay by viewModel.totalsForDay.collectAsState(initial = TotalsForDay(0.0, 0.0, 0.0, 0.0))
    val mealDates by viewModel.mealDates.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    val density = LocalDensity.current
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val (selectedIndex, centerOffset) = viewModel.calculateScrollingValuesForSelectedDate(mealDates, selectedDate, screenWidthPx)
    val lazyListState = rememberLazyListState()

    LaunchedEffect(selectedIndex, centerOffset) {
        if (selectedIndex >= 0) {
            lazyListState.scrollToItem(selectedIndex)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        bottomBar = {
            Button(
                onClick = { viewModel.addMeal(selectedDate) },
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
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
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
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Choose day",
                                tint = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    state = lazyListState,
                    contentPadding = PaddingValues(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(mealDates) { dateFor ->
                        val isToday = viewModel.isToday(dateFor)
                        WeekdayButton(
                            dateFor = dateFor,
                            onClick = { selected ->
                                viewModel.updateSelectedDate(selected)
                            },
                            viewModel = viewModel,
                            isToday = isToday,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                LaunchedEffect(lazyListState) {
                    snapshotFlow { lazyListState.firstVisibleItemIndex }
                        .collect { index ->
                            val layoutInfo = lazyListState.layoutInfo

                            if (index < layoutInfo.totalItemsCount - 1) {
                                lazyListState.animateScrollToItem(index)
                            } else if (index > 0) {
                                lazyListState.animateScrollToItem(index - 1)
                            }
                        }

                }
                Spacer(modifier = Modifier.height(16.dp))

                meals.forEach { (mealId, mealNumber) ->
                    MealItem(
                        mealId = mealId,
                        mealNumber = mealNumber,
                        navController = navController,
                        viewModel = viewModel,
                        selectedDate = viewModel.selectedDate.value
                    )
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
    mealNumber: Int,
    navController: NavHostController,
    viewModel: MainViewModel,
    selectedDate: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Posiłek $mealNumber",
            style = MaterialTheme.typography.labelMedium
        )

        Row {
            IconButton(
                onClick = {
                    viewModel.selectMeal(mealId, selectedDate)
                    navController.navigate("categories")
                },
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
                onClick = { viewModel.deleteMeal(mealId, selectedDate) },
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