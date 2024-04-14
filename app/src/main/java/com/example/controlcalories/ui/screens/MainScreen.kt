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

        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            CategoryButton(navController = navController) // Umieszczamy przycisk do wyboru kategorii

            // Reszta UI, np. przyciski dodawania posiłków
            Spacer(modifier = Modifier.height(16.dp))
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
fun CategoryButton(navController: NavHostController) {
    Button(
        onClick = { navController.navigate("categories") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = buttonColors(containerColor = defaultButtonColor)
    ) {
        Text("Wybierz kategorię", color = Color.White)
    }
}

@Composable
fun CategoryItem(category: ProductCategory, viewModel: MainViewModel, isExpanded: Boolean) {
    // Logika dla rozwijania/zwiń kategorii
    Column(modifier = Modifier
        .clickable {
            viewModel.toggleCategoryExpansion(category.id)
        }
        .padding(8.dp)
    ) {
        Text(text = category.name, style = Typography.labelSmall)
        AnimatedVisibility(visible = isExpanded) {
        }
    }
}

@Composable
fun QuantityInputDialog(viewModel: MainViewModel) {
    val weightText = remember { mutableStateOf("") }

    // Pobierz wybrany produkt z ViewModel
    val selectedProduct = viewModel.selectedProduct.collectAsState().value

    // Jeżeli nie ma wybranego produktu, nie pokazuj dialogu
    if (selectedProduct == null || !viewModel.showQuantityDialog.collectAsState().value) {
        return
    }

    Dialog(onDismissRequest = { viewModel.toggleQuantityDialog(false) }) {
        Surface(
            shape = MaterialTheme.shapes.medium, // Zaokrąglone rogi
            shadowElevation = 8.dp // Cień dialogu
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 16.dp) // Dodaj padding wewnątrz dialogu
                    .wrapContentSize() // Rozmiar wg zawartości
            ) {
                Text(
                    text = "Podaj ilość dla produktu ${selectedProduct.name}",
                    style = Typography.labelSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = weightText.value,
                    onValueChange = { newText ->
                        if (newText.all { it.isDigit() || it == '.' }) {
                            weightText.value = newText
                        }
                    },
                    label = { Text("Ilość (w gramach)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Konwertuj tekst na liczbę i dodaj produkt
                        weightText.value.toFloatOrNull()?.let { weight ->
                            if (weight > 0) {
                                viewModel.addUserProduct(weight, selectedProduct.categoryId)
                                viewModel.toggleQuantityDialog(false) // Zamknij dialog
                            } else {
                                // Obsłuż błąd: wprowadzono nieprawidłową wagę
                                // Można wyświetlić jakiś komunikat błędu itp.
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth() // Przycisk na całą szerokość
                ) {
                    Text("Dodaj")
                }
            }
        }
    }
}
@Composable
fun CategoriesDialog(viewModel: MainViewModel, onCategorySelected: (Int) -> Unit) {
    Dialog(onDismissRequest = { viewModel.toggleCategoryDialog(false) }) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Wybierz kategorię",
                    style = Typography.labelLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                val categories = viewModel.categories.collectAsState().value
                LazyColumn {
                    items(categories) { category ->
                        CategoryListItem(category = category, onCategorySelected = onCategorySelected)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(defaultButtonColor)
                        .clickable { viewModel.toggleCategoryDialog(false) }
                        .padding(12.dp)
                ) {
                    Text(
                        "Anuluj",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = Typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryListItem(category: ProductCategory, onCategorySelected: (Int) -> Unit) {
    Text(
        text = category.name,
        modifier = Modifier
            .clickable { onCategorySelected(category.id) }
            .padding(10.dp),
        style = MaterialTheme.typography.titleSmall
    )
}
@Composable
fun ProductList(categoryId: Int, viewModel: MainViewModel) {
    val products = viewModel.products.collectAsState().value.filter { it.categoryId == categoryId }
    LazyColumn {
        items(products) { product ->
            ProductItem(product = product, viewModel = viewModel)
        }
    }
}
@Composable
fun ProductItem(product: Product, viewModel: MainViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                viewModel.selectProduct(product)
                viewModel.toggleQuantityDialog(true)
            },
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.name,
                style = Typography.labelMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${product.calories} kcal",
                style = Typography.labelMedium
            )
        }
    }
}

