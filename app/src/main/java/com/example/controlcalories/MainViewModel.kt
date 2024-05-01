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
import com.example.controlcalories.data.model.dto.TotalsForDay
import com.example.controlcalories.data.model.dto.UserProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.round

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val database: ProductDatabase = ProductDatabase.getInstance(app.applicationContext)
    private val productDao = database.productDao()
    private val userProductDao = database.userProductDao()
    private val mealDao = database.mealDao()
    private val sharedPreferences = app.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val totalsForDay: Flow<TotalsForDay> = userProductDao.getTotalsForDay(getTodayDateString())
    var categories = MutableStateFlow<List<ProductCategory>>(emptyList())
    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products
    private val _currentMealId = MutableStateFlow<Int?>(null)
    val currentMealId: StateFlow<Int?> = _currentMealId.asStateFlow()
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()
    private val _meals = MutableStateFlow<List<Pair<Int, String>>>(emptyList())
    val meals: StateFlow<List<Pair<Int, String>>> = _meals
    private val _mealsByDay =
        MutableStateFlow<Map<String, MutableList<Pair<Int, String>>>>(emptyMap())
    val mealsByDay: StateFlow<Map<String, List<Pair<Int, String>>>> = _mealsByDay

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

    private fun getTodayDateString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
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

    fun addMeal() {
        val calendar = Calendar.getInstance()
        val dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekString = mapDayOfWeekToString(dayOfWeekNumber)
        val nextMealNumber = _meals.value.size + 1
        val newMealName = "Posiłek $nextMealNumber"
        val currentDateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        viewModelScope.launch {
            try {
                val meal = Meal(
                    name = newMealName,
                    dayOfWeek = dayOfWeekString,
                    mealNumber = nextMealNumber,
                    dateFor = currentDateStr
                )
                val mealId = mealDao.insertMeal(meal).toInt()
                Log.d("ViewModel", "Meal added successfully with ID: $mealId")

                val updatedMeals = _meals.value.toMutableList()
                updatedMeals.add(mealId to newMealName)
                _meals.value = updatedMeals

                updateMealsByDay(dayOfWeekString, mealId, newMealName)
                loadTodayMeals()
            } catch (e: Exception) {
                Log.e("ViewModel", "Error adding meal: ${e.message}")
                showErrorAlert.value = true
            }
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

    fun addUserProduct(weight: Float, categoryId: Int, mealId: Int, onProductAdded: () -> Unit) {
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
                dateFor = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
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

    fun getStartAndEndOfDay(dateInMillis: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }

    fun loadTodayMeals() {
        val currentMillis = System.currentTimeMillis()
        val (startOfDay, endOfDay) = getStartAndEndOfDay(currentMillis)

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedStartOfDay = formatter.format(Date(startOfDay))
        val formattedEndOfDay = formatter.format(Date(endOfDay))

        Log.d("ViewModel", "Loading meals from $formattedStartOfDay to $formattedEndOfDay")

        viewModelScope.launch {
            mealDao.getMealsByDate(formattedStartOfDay, formattedEndOfDay).collect { meals ->
                Log.d("ViewModel", "Loaded ${meals.size} meals")
                val newMealsMap = meals.groupBy { meal ->
                    mapDayOfWeekToString(mapStringDayToInt(meal.dayOfWeek) ?: return@groupBy "")
                }.mapValues { entry ->
                    entry.value.map { it.mealId to it.name }.toMutableList()
                }.toMutableMap()

                _mealsByDay.emit(newMealsMap)
                _meals.emit(meals.map { it.mealId to it.name })
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

    fun deleteMeal(mealId: Int) {
        viewModelScope.launch {
            mealDao.deleteMeal(mealId)
            userProductDao.deleteProductsWithMeal(mealId)
        }
    }

    fun getProductsFromMeal(mealId: Int): Flow<List<UserProduct>> {
        return userProductDao.getProductsFromMeal(mealId)
    }

    fun getProductsByCategory(categoryId: Int): Flow<List<Product>> {
        return productDao.getProductsByCategory(categoryId)
    }

    fun getMealIdForDay(day: String): Int? {
        val currentDay = when (day) {
            "Pon" -> Calendar.MONDAY
            "Wt" -> Calendar.TUESDAY
            "Śr" -> Calendar.WEDNESDAY
            "Czw" -> Calendar.THURSDAY
            "Pt" -> Calendar.FRIDAY
            "Sob" -> Calendar.SATURDAY
            "Ndz" -> Calendar.SUNDAY
            else -> return null
        }

        val dayOfWeekString = mapDayOfWeekToString(currentDay)
        val mealsList = _mealsByDay.value[dayOfWeekString]

        return mealsList?.firstOrNull()?.first
    }

    fun updateMealsByDay(dayOfWeekString: String, mealId: Int, newMealName: String) {
        val currentMealsByDay = _mealsByDay.value.toMutableMap()
        val mealsList = currentMealsByDay.getOrDefault(dayOfWeekString, mutableListOf())

        val index = mealsList.indexOfFirst { it.first == mealId }
        if (index != -1) {
            mealsList[index] = Pair(mealId, newMealName)
        } else {
            mealsList.add(Pair(mealId, newMealName))
        }
        currentMealsByDay[dayOfWeekString] = mealsList
        _mealsByDay.value = currentMealsByDay

        Log.d("ViewModel", "Meals by day updated: $currentMealsByDay")
    }

    fun selectMeal(mealId: Int) {
        _currentMealId.value = mealId
        Log.d("ViewModel", "Current meal ID set to $mealId")
    }

}



