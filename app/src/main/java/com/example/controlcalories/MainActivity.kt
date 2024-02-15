package com.example.controlcalories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.controlcalories.ui.screens.StartScreen
import com.example.controlcalories.ui.theme.ControlCaloriesTheme
import com.example.controlcalories.ui.theme.defaultStartScreenColor


class MainActivity : ComponentActivity() {

    private val mainVm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ControlCaloriesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
}

@Composable
fun MainApp(viewModel: MainViewModel) {
    ControlCaloriesTheme {
        val navController = rememberNavController()
        NavigationHost(
            vm = viewModel,
            navHostController = navController
        )
    }
}

@Composable
fun NavigationHost(vm: MainViewModel, navHostController: NavHostController) {

}
