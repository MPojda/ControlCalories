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
import com.example.controlcalories.data.model.dto.MealWithProducts
import com.example.controlcalories.data.model.dto.Product
import com.example.controlcalories.data.model.dto.ProductCategory
import com.example.controlcalories.data.model.dto.ProductDatabase
import com.example.controlcalories.data.model.dto.TotalsForDay
import com.example.controlcalories.data.model.dto.UserProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.round

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val database: ProductDatabase = ProductDatabase.getInstance(app.applicationContext)
    private val productDao = database.productDao()
    private val userProductDao = database.userProductDao()
    private val mealDao = database.mealDao()
    private val sharedPreferences = app.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    private val _selectedDate = MutableStateFlow(LocalDate.now().toString())
    val selectedDate: StateFlow<String> = _selectedDate
    val totalsForDay: Flow<TotalsForDay> = selectedDate.flatMapLatest { selectedDate ->
        userProductDao.getTotalsForDay(selectedDate)
    }
    var categories = MutableStateFlow<List<ProductCategory>>(emptyList())
    private val _selectedCategoryId = MutableStateFlow<Int?>(null)

    private val _mealDates = MutableStateFlow<List<String>>(emptyList())
    val mealDates: StateFlow<List<String>> = _mealDates
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products
    private val _currentMealId = MutableStateFlow<Int?>(null)
    val currentMealId: StateFlow<Int?> = _currentMealId.asStateFlow()
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()
    private val _meals = MutableStateFlow<List<Pair<Int, Int>>>(emptyList())
    val meals: StateFlow<List<Pair<Int, Int>>> = _meals
    private val _mealsByDay = MutableStateFlow<Map<String, MutableList<Pair<Int, Int>>>>(emptyMap())
    val mealsByDay: StateFlow<Map<String, List<Pair<Int, Int>>>> = _mealsByDay
    private val _mealsWithProducts = MutableStateFlow<List<MealWithProducts>>(emptyList())
    val mealsWithProducts: StateFlow<List<MealWithProducts>> = _mealsWithProducts

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
        loadMealDates()
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

    private fun mapDayOfWeekToString(dayOfWeekNumber: Int): String {
        return when (dayOfWeekNumber) {
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

    fun mapStringDayToInt(day: String): Int? {
        return when (day) {
            "Pon" -> Calendar.MONDAY
            "Wt" -> Calendar.TUESDAY
            "Śr" -> Calendar.WEDNESDAY
            "Czw" -> Calendar.THURSDAY
            "Pt" -> Calendar.FRIDAY
            "Sob" -> Calendar.SATURDAY
            "Ndz" -> Calendar.SUNDAY
            else -> null
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

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateAsString: String = LocalDate.now().format(dateFormatter)

    fun addMeal(selectedDate: String) {
        val calendar = Calendar.getInstance()
        val dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekString = mapDayOfWeekToString(dayOfWeekNumber)

        viewModelScope.launch {
            try {
                val mealsForDate = mealDao.getMealsForDate(selectedDate)
                val nextMealNumber = mealsForDate.size + 1
                val newMealName = "Posiłek $nextMealNumber"

                val meal = Meal(
                    name = newMealName,
                    dayOfWeek = dayOfWeekString,
                    mealNumber = nextMealNumber,
                    dateFor = selectedDate
                )
                val mealId = mealDao.insertMeal(meal).toInt()
                Log.d("ViewModel", "Meal added successfully with ID: $mealId")

                loadMealsForDate(selectedDate)

            } catch (e: Exception) {
                Log.e("ViewModel", "Error adding meal: ${e.message}")
                showErrorAlert.value = true
            }
        }
    }

    fun deleteMeal(mealId: Int, selectedDate: String) {
        viewModelScope.launch {
            try {
                val mealToDelete = mealDao.getMealByID(mealId)

                if (mealToDelete != null) {
                    mealDao.deleteMeal(mealId)
                    mealDao.deleteAllProductsForMeal(mealId)

                    val remainingMeals = mealDao.getMealsWithHigherNumbers(selectedDate, mealToDelete.mealNumber)

                    remainingMeals.forEachIndexed { index, meal ->
                        mealDao.updateMealNumber(meal.mealId, mealToDelete.mealNumber + index)
                    }

                    loadMealsForDate(selectedDate)
                } else {
                    Log.e("ViewModel", "Meal with ID $mealId not found")
                }

            } catch (e: Exception) {
                Log.e("ViewModel", "Error deleting meal: ${e.message}")
                showErrorAlert.value = true
            }
        }
    }

    fun addUserProduct(
        weight: Float,
        categoryId: Int,
        mealId: Int,
        selectedDate: String,
        onProductAdded: () -> Unit
    ) {
        val product = _selectedProduct.value
        if (product != null && weight > 0) {
            val newUserProduct = UserProduct(
                name = product.name,
                calories = round((product.calories * weight / 100) * 10) / 10.0,
                protein = round((product.protein * weight / 100) * 10) / 10.0,
                fat = round((product.fat * weight / 100) * 10) / 10.0,
                carbohydrates = round((product.carbohydrates * weight / 100) * 10) / 10.0,
                sugar = round((product.sugar * weight / 100) * 10) / 10.0,
                fiber = round((product.fiber * weight / 100) * 10) / 10.0,
                dateFor = selectedDate,
                weight = weight,
                categoryId = categoryId,
                mealId = mealId
            )
            viewModelScope.launch {
                userProductDao.insert(newUserProduct)
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

    private fun loadMealsForDate(selectedDate: String) {
        viewModelScope.launch {
            try {
                val mealsForDate = mealDao.getMealsForDate(selectedDate)

                _meals.value = mealsForDate.map { it.mealId to it.mealNumber }

                val groupedMeals = mealsForDate.groupBy { meal ->
                    mapDayOfWeekToString(mapStringDayToInt(meal.dayOfWeek) ?: return@groupBy "")
                }.mapValues { entry ->
                    entry.value.map { it.mealId to it.mealNumber }.toMutableList()
                }.toMutableMap()

                _mealsByDay.emit(groupedMeals)

            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading meals: ${e.message}")
                showErrorAlert.value = true
            }
        }
    }


    fun updateProductInMeal(userProduct: UserProduct, newWeight: Float) {
        viewModelScope.launch {
            if (userProduct.weight > 0 && newWeight > 0) {
                val factor = newWeight / userProduct.weight

                val updatedProduct = userProduct.copy(
                    weight = newWeight,
                    calories = roundValue(userProduct.calories * factor),
                    protein = roundValue(userProduct.protein * factor),
                    fat = roundValue(userProduct.fat * factor),
                    carbohydrates = roundValue(userProduct.carbohydrates * factor),
                    sugar = roundValue(userProduct.sugar * factor),
                    fiber = roundValue(userProduct.fiber * factor)
                )

                userProductDao.update(updatedProduct)
            } else {
                Log.e(
                    "ViewModel",
                    "Nieprawidłowe dane wejściowe: newWeight = $newWeight lub oryginalna waga = ${userProduct.weight}"
                )
            }
        }
    }

    fun roundValue(value: Double): Double {
        return (Math.round(value * 10) / 10.0)
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


    fun selectMeal(mealId: Int, selectedDate: String) {
        _currentMealId.value = mealId
        Log.d("ViewModel", "Current meal ID set to $mealId")
    }

    fun getPreviousDays(): List<Pair<String, Int>> {
        val dateFormat = SimpleDateFormat("EEE")
        val calendar = Calendar.getInstance()
        val days = mutableListOf<Pair<String, Int>>()

        calendar.add(Calendar.DATE, -14)

        for (i in 0 until 29) {
            val dayOfWeekName = dateFormat.format(calendar.time)
            days.add(Pair(dayOfWeekName, calendar.get(Calendar.DAY_OF_MONTH)))
            calendar.add(Calendar.DATE, 1)
        }

        return days
    }

    fun main() {
        val previousDays = getPreviousDays()
        for (day in previousDays) {
            println("${day.first}, ${day.second}")
        }
    }

    fun updateSelectedDate(selectedDate: String) {
        _selectedDate.value = selectedDate
        loadMealsForDate(selectedDate)
    }

    fun isToday(dateFor: String): Boolean {
        return dateFor == _selectedDate.value
    }

    fun calculateScrollingValuesForSelectedDate(
        dates: List<String>,
        selectedDate: String,
        screenWidthPx: Float
    ): Pair<Int, Int> {
        val selectedIndex = dates.indexOf(selectedDate)

        val centerOffset = if (selectedIndex >= 0) {
            val itemWidthPx = screenWidthPx / dates.size
            val itemOffsetPx = selectedIndex * itemWidthPx
            val halfScreenWidthPx = screenWidthPx / 2
            val centerOffset = itemOffsetPx - halfScreenWidthPx + (itemWidthPx / 2)
            centerOffset.toInt()
        } else {
            0
        }

        return Pair(selectedIndex, centerOffset)
    }

    private fun loadMealDates() {
        val today = LocalDate.now()
        val startDate = today.minusDays(28)
        val endDate = today.plusDays(2)

        val dateList = generateSequence(startDate) { it.plusDays(1) }
            .takeWhile { it <= endDate }
            .map { it.toString() }
            .toList()

        _mealDates.value = dateList
    }

}



