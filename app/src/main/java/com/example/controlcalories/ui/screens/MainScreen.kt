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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.controlcalories.MainViewModel
import com.example.controlcalories.R
import com.example.controlcalories.data.model.dto.Product
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

            // Użyj Box jako kontenera dla IconButton, aby ustawić tło i kształt
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

                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Powrót",
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
            WeekdayButton(text = "Pon", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Wt", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Śr", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Czw", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Pt", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Sob", onClick = { /* Handle button click */ })
            WeekdayButton(text = "Ndz", onClick = { /* Handle button click */ })
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

        LazyColumn {
            itemsIndexed(meals) { index, meal ->
                MealItem(meal, index, viewModel, expandedMeals.getOrDefault(index, false))
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = { viewModel.addMeal() },
                        modifier = Modifier.padding(8.dp),
                        colors = buttonColors(
                            containerColor = defaultButtonColor
                        )
                    ) {
                        Text("Dodaj posiłek", color = Color.White)
                    }
                }
            }
        }
        if (viewModel.showCategoryDialog.value) {
            CategorySelectionDialog(viewModel = viewModel)
        }

        if (viewModel.showProductDialog.value && viewModel.selectedCategory.value != null) {
            ProductSelectionDialog(viewModel = viewModel, categoryId = viewModel.selectedCategory.value!!.id)
        }

        if (viewModel.showQuantityDialog.value && viewModel.selectedProduct.value != null) {
            QuantityInputDialog(viewModel = viewModel)
        }
    }
}

@Composable
fun MealItem(meal: String, mealId: Int, viewModel: MainViewModel, isExpanded: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = meal,
                style = MaterialTheme.typography.labelLarge
            )

            Box(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(defaultButtonColor)
                    .align(Alignment.CenterVertically),
            ) {
                IconButton(
                    onClick = {
                        // Tutaj wywołujemy funkcję z ViewModel, aby otworzyć dialog wyboru produktu.
                        viewModel.toggleShowDialog(true)
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Dodaj produkt",
                        tint = Color.White
                    )
                }
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column {
            }
        }
    }
}
@Composable
fun CategorySelectionDialog(viewModel: MainViewModel) {
    Dialog(onDismissRequest = { viewModel.toggleCategoryDialog(false) }) {
        // UI dialogu
        LazyColumn {
            items(viewModel.categories.value) { category ->
                Text(
                    text = category.name,
                    modifier = Modifier.clickable {
                        viewModel.selectedCategory.value = category
                        viewModel.toggleCategoryDialog(false)
                        viewModel.toggleProductDialog(true)
                    }.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ProductSelectionDialog(viewModel: MainViewModel, categoryId: Int) {
    Dialog(onDismissRequest = { viewModel.toggleProductDialog(false) }) {
        // UI dialogu
        val productsInCategory = viewModel.products.value.filter { it.uid == categoryId }
        LazyColumn {
            items(productsInCategory) { product ->
                Text(
                    text = product.name,
                    modifier = Modifier.clickable {
                        viewModel.selectedProduct.value = product
                        viewModel.toggleProductDialog(false)
                        viewModel.toggleQuantityDialog(true)
                    }.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun QuantityInputDialog(viewModel: MainViewModel) {
    Dialog(onDismissRequest = { viewModel.toggleQuantityDialog(false) }) {
        // UI dialogu
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = viewModel.productQuantity.value,
                onValueChange = { quantity -> viewModel.productQuantity.value = quantity },
                label = { Text("Ilość (w gramach)") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                viewModel.addProductToMeal()
                viewModel.toggleQuantityDialog(false)
            }) {
                Text("Dodaj")
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
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelLarge
        )

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
fun CategoriesList(viewModel: MainViewModel) {
    val categories by viewModel.categories.collectAsState()
    val expandedCategories by viewModel.expandedCategories.collectAsState()

    LazyColumn {
        items(categories) { category ->
            CategoryItem(category, viewModel, expandedCategories[category.id] ?: false)
        }
    }
}

@Composable
fun CategoryItem(category: ProductCategory, viewModel: MainViewModel, isExpanded: Boolean) {
    Column(modifier = Modifier.clickable { viewModel.toggleCategoryExpansion(category.id) }) {
        Text(text = category.name)
        AnimatedVisibility(visible = isExpanded) {
            ProductsList(categoryId = category.id, viewModel = viewModel)
        }
    }
}

@Composable
fun ProductsList(categoryId: Int, viewModel: MainViewModel) {
    // Tutaj potrzebujesz mechanizmu do filtrowania produktów na podstawie categoryId
    // oraz logiki do ich wyświetlenia
}

@Composable
fun ProductItem(product: Product, viewModel: MainViewModel) {
    // UI dla pojedynczego produktu
    Text(
        text = product.name,
        modifier = Modifier.clickable { /* Otwórz dialog do dodawania ilości */ }
    )
}

