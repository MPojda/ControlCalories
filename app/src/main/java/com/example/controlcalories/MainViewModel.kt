package com.example.controlcalories

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.controlcalories.data.model.domain.calculateAge
import com.example.controlcalories.data.model.domain.calculateBMI
import com.example.controlcalories.data.model.domain.getBMICategory
import com.example.controlcalories.data.model.dto.Meal
import com.example.controlcalories.data.model.dto.Product
import com.example.controlcalories.data.model.dto.ProductCategory
import com.example.controlcalories.data.model.dto.ProductDatabase
import com.example.controlcalories.data.model.dto.UserProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.roundToInt

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val database: ProductDatabase = ProductDatabase.getInstance(app.applicationContext)
    private val productDao = database.productDao()
    private val userProductDao = database.userProductDao()
    private val mealDao = database.mealDao()
    private val sharedPreferences = app.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    var categories = MutableStateFlow<List<ProductCategory>>(emptyList())

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()


    private val _meals = MutableStateFlow<List<String>>(emptyList())
    val meals: StateFlow<List<String>> = _meals


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

    fun isToday(weekday: String): Boolean {
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


    private fun mapDayOfWeekToString(dayOfWeekNumber: Int): String {
        return when(dayOfWeekNumber) {
            Calendar.MONDAY -> "Pon"
            Calendar.TUESDAY -> "Wt"
            Calendar.WEDNESDAY -> "Śr"
            Calendar.THURSDAY -> "Czw"
            Calendar.FRIDAY -> "Pt"
            Calendar.SATURDAY -> "Sob"
            Calendar.SUNDAY -> "Ndz"
            else -> "Nieznany"
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                productDao.getAllProducts().collectLatest { productList ->
                    _products.value = productList
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading products: ${e.message}")
                showErrorAlert.value = true
            }
        }
    }

    private fun loadCategories() {
        categories.value = listOf(
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
            ProductCategory(13, "Chipsy/Przyprawy", 477, 479),
            ProductCategory(14, "Dania gotowe", 480, 654)
        )
    }

    fun addMeal() {
        val calendar = Calendar.getInstance()
        val dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekString = mapDayOfWeekToString(dayOfWeekNumber)
        val nextMealNumber = _meals.value.size + 1
        val newMealName = "Posiłek $nextMealNumber"

        viewModelScope.launch {
            val meal = Meal(name = newMealName, dayOfWeek = dayOfWeekString, mealNumber = nextMealNumber)
            val mealId = mealDao.insertMeal(meal).toInt()
            val newUserProduct = UserProduct(
                name = newMealName,
                calories = 0.0,
                protein = 0.0,
                fat = 0.0,
                carbohydrates = 0.0,
                sugar = 0.0,
                fiber = 0.0,
                weight = 0f,
                categoryId = 0,
                mealId = mealId
            )
            userProductDao.insert(newUserProduct)
            loadTodayMeals()
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

    fun addUserProduct(weight: Float, categoryId: Int, onProductAdded: () -> Unit) {
        val product = _selectedProduct.value
        if (product != null && weight > 0) {
            val userProduct = UserProduct(
                id = 0,
                name = product.name,
                calories = (product.calories * weight / 100 * 10).roundToInt() / 10.0,
                protein = (product.protein * weight / 100 * 10).roundToInt() / 10.0,
                fat = (product.fat * weight / 100 * 10).roundToInt() / 10.0,
                carbohydrates = (product.carbohydrates * weight / 100 * 10).roundToInt() / 10.0,
                sugar = (product.sugar * weight / 100 * 10).roundToInt() / 10.0,
                fiber = (product.fiber * weight / 100 * 10).roundToInt() / 10.0,
                addedDate = System.currentTimeMillis(),
                weight = weight,
                categoryId = categoryId,
                mealId = 0
            )

            viewModelScope.launch {
                userProductDao.insert(userProduct)
                onProductAdded()
            }
        }
    }

    fun selectCategory(categoryId: Int) {
        _selectedCategoryId.value = categoryId
    }
    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }
    fun getStartAndEndOfDay(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }

    fun loadTodayMeals() {
        val (startOfDay, endOfDay) = getStartAndEndOfDay()
        viewModelScope.launch {
            mealDao.getMealsByDate(startOfDay, endOfDay).collect { meals ->
                _meals.value = meals.map { it.name }
            }
        }
    }
    fun updateProductWeight(userProduct: UserProduct, newWeight: Float) {
        viewModelScope.launch {
            val updatedProduct = userProduct.copy(weight = newWeight)
            userProductDao.update(updatedProduct)
        }
    }

    fun deleteProductFromMeal(userProduct: UserProduct) {
        viewModelScope.launch {
            userProductDao.delete(userProduct)
        }
    }

    fun getProductsFromMeal(mealId: Int): Flow<List<UserProduct>> {
        return userProductDao.getProductsFromMeal(mealId)
    }
    fun getProductsByCategory(categoryId: Int): Flow<List<Product>> {
        return productDao.getProductsByCategory(categoryId)
    }
}



