package com.example.controlcalories

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.controlcalories.data.model.domain.calculateAge
import com.example.controlcalories.data.model.domain.calculateBMI
import com.example.controlcalories.data.model.domain.getBMICategory
import com.example.controlcalories.data.model.dto.Product
import com.example.controlcalories.data.model.dto.ProductCategory
import com.example.controlcalories.data.model.dto.ProductDatabase
import com.example.controlcalories.data.model.dto.UserProductDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val database: ProductDatabase = ProductDatabase.getInstance(app.applicationContext)
    private val productDao = database.productDao()
    private val userProductDao = database.userProductDao()


    private val sharedPreferences = app.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    private val _categories = MutableStateFlow<List<ProductCategory>>(emptyList())
    val categories: StateFlow<List<ProductCategory>> = _categories

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _expandedMeals = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val expandedMeals: StateFlow<Map<Int, Boolean>> = _expandedMeals

    private val _meals = MutableStateFlow<List<String>>(listOf("Posiłek 1"))
    val meals: StateFlow<List<String>> = _meals

    private val _expandedCategories = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val expandedCategories: StateFlow<Map<Int, Boolean>> = _expandedCategories

    val showDialog = mutableStateOf(false)
    val showCategoryDialog = mutableStateOf(false)
    val showProductDialog = mutableStateOf(false)
    val showQuantityDialog = mutableStateOf(false)
    val selectedCategory = mutableStateOf<ProductCategory?>(null)
    val selectedProduct = mutableStateOf<Product?>(null)
    val productQuantity = mutableStateOf("")

    var showErrorAlert = MutableStateFlow(false)
    var gender = MutableStateFlow("")
    var dayOfBirth = MutableStateFlow("")
    var monthOfBirth = MutableStateFlow("")
    var yearOfBirth = MutableStateFlow("")
    var height = MutableStateFlow("")
    var weight = MutableStateFlow("")
    var bmiResult = MutableStateFlow<Float?>(null)
    var bmiCategory = MutableStateFlow<String?>(null)

    var isDayValid = MutableStateFlow(true)
    var isMonthValid = MutableStateFlow(true)
    var isYearValid = MutableStateFlow(true)
    var isWeightValid = MutableStateFlow(true)
    var isHeightValid = MutableStateFlow(true)
    var showResultDialog = MutableStateFlow(false)


    init {
        loadProducts()
        loadCategories()
        loadUserData()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productDao.getAllProducts().collectLatest { productList ->
                _products.value = productList
            }
        }
    }

    private fun loadCategories() {
        _categories.value = listOf(
            ProductCategory(1, "Nabiał", 1, 56),
            ProductCategory(2, "Jaja", 57, 61),
            ProductCategory(3, "Mięso", 62, 102),
            ProductCategory(4, "Wędliny", 103, 148),
            ProductCategory(5, "Ryby", 149, 184),
            ProductCategory(6, "Tłuszcze", 185, 204),
            ProductCategory(7, "Mąka/Pieczywo", 205, 259),
            ProductCategory(8, "Warzywa", 260, 336),
            ProductCategory(9, "Owoce", 337, 390),
            ProductCategory(10, "Orzechy", 391, 399),
            ProductCategory(11, "Słodycze", 400, 457),
            ProductCategory(12, "Soki/Napoje", 458, 476),
            ProductCategory(13, "Chipsy", 477, 479),
            ProductCategory(14, "Dania gotowe", 480, 654)
        )
    }

    fun addMeal() {
        val nextMealNumber = _meals.value.size + 1
        _meals.value = _meals.value + "Posiłek $nextMealNumber"
    }

    fun toggleMealExpansion(mealId: Int) {

        val currentState = _expandedMeals.value[mealId] ?: false
        _expandedMeals.value = _expandedMeals.value.toMutableMap().apply {
            this[mealId] = !currentState
        }
    }


    fun saveUserData() {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("gender", gender.value)
            putString("dayOfBirth", dayOfBirth.value)
            putString("monthOfBirth", monthOfBirth.value)
            putString("yearOfBirth", yearOfBirth.value)
            putString("height", height.value)
            putString("weight", weight.value)
            apply()
        }
    }


    private fun loadUserData() {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        gender.value = sharedPreferences.getString("gender", "") ?: ""
        dayOfBirth.value = sharedPreferences.getString("dayOfBirth", "") ?: ""
        monthOfBirth.value = sharedPreferences.getString("monthOfBirth", "") ?: ""
        yearOfBirth.value = sharedPreferences.getString("yearOfBirth", "") ?: ""
        height.value = sharedPreferences.getString("height", "") ?: ""
        weight.value = sharedPreferences.getString("weight", "") ?: ""
    }

    fun calculateAndSaveBMI() {

        val heightInCm = height.value.toIntOrNull()
        val weightInKg = weight.value.toIntOrNull()
        val day = dayOfBirth.value.toIntOrNull()
        val month = monthOfBirth.value.toIntOrNull()
        val year = yearOfBirth.value.toIntOrNull()

        if (heightInCm != null && weightInKg != null && day != null && month != null && year != null && heightInCm > 0 && weightInKg > 0) {
            val dateOfBirth = LocalDate.of(year, month, day)
            val age = calculateAge(dateOfBirth)

            val bmi = calculateBMI(heightInCm, weightInKg, age)
            if (bmi >= 0) {
                bmiResult.value = bmi
                bmiCategory.value = getBMICategory(bmi, age)
                showErrorAlert.value = false
                showResultDialog.value = true
                saveUserData()
            } else {

                showErrorAlert.value = true
            }
        } else {

            showErrorAlert.value = true
        }
    }
    fun updateDayOfBirth(newDay: String) {
        this.dayOfBirth.value = newDay
        saveUserData()

        val dayInt = newDay.toIntOrNull()
        isDayValid.value = dayInt in 1..31
    }
    fun updateMonthOfBirth(newMonth: String) {
        this.monthOfBirth.value = newMonth
        saveUserData()

        val monthInt = newMonth.toIntOrNull()
        isMonthValid.value = monthInt in 1..12
    }
    fun updateYearOfBirth(newYear: String) {
        this.yearOfBirth.value = newYear
        saveUserData()

        val yearInt = newYear.toIntOrNull()
        isYearValid.value = yearInt in 1900..LocalDate.now().year
    }
    fun updateWeight(newWeight: String) {
        this.weight.value = newWeight
        saveUserData()

        val weightInt = newWeight.toIntOrNull()
        isWeightValid.value = weightInt in 1..400
    }
    fun updateHeight(newHeight: String) {
        this.height.value = newHeight
        saveUserData()

        val heightInt = newHeight.toIntOrNull()
        isHeightValid.value = heightInt in 80..300
    }
    fun updateGender(newGender: String) {
        gender.value = newGender
        saveUserData()
    }
    fun isFirstTimeUser(): Boolean {
        return sharedPreferences.getBoolean("isFirstTimeUser", true)
    }

    fun setUserNotFirstTime() {
        with(sharedPreferences.edit()) {
            putBoolean("isFirstTimeUser", false)
            apply()
        }
    }
    fun toggleShowDialog(show: Boolean) {
        showDialog.value = show
    }
    fun toggleCategoryDialog(show: Boolean) {
        showCategoryDialog.value = show
    }

    fun toggleProductDialog(show: Boolean) {
        showProductDialog.value = show
        if (!show) {
            // Resetuj wybrany produkt i ilość, gdy dialog się zamyka
            selectedProduct.value = null
            productQuantity.value = ""
        }
    }

    fun toggleQuantityDialog(show: Boolean) {
        showQuantityDialog.value = show
    }

    fun addProductToMeal() {
        selectedProduct.value = null
        productQuantity.value = ""
    }
    fun toggleCategoryExpansion(categoryId: Int) {
        val currentState = _expandedCategories.value[categoryId] ?: false
        _expandedCategories.value = _expandedCategories.value.toMutableMap().apply {
            this[categoryId] = !currentState
        }
    }
    fun addProductToMeal(productId: Int, quantity: String) {

    }

}


