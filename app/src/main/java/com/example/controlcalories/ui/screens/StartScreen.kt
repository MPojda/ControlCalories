package com.example.controlcalories.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.controlcalories.R
import com.example.controlcalories.ui.default_components.Button
import com.example.controlcalories.ui.theme.Typography
import com.example.controlcalories.ui.theme.defaultIncorrectColor


@Composable
fun StartScreen(
    modifier: Modifier.Companion = Modifier,
    navController: NavHostController,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {

        Image(
            painter = painterResource(id = R.drawable.pizza),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color.Transparent
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .size(200.dp, 200.dp),

                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Jaki jest Twój cel ?",
                    style = Typography.labelLarge,
                    fontSize = 32.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(defaultIncorrectColor)

                )


                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(280.dp, 60.dp),
                    text = "Utrata masy ciała",
                    textStyle = Typography.labelLarge,
                    onClick = { navController.navigate("bmi") }
                )
                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(280.dp, 60.dp),
                    text = "Utrzymanie wagi",
                    textStyle = Typography.labelLarge,
                    onClick = { navController.navigate("bmi") }
                )
                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(280.dp, 60.dp),
                    text = "Przyrost masy ciała",
                    textStyle = Typography.labelLarge,
                    onClick = { navController.navigate("bmi") }
                )
            }
        }
    }
}
@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController)
        }
        composable("bmi") {
            BmiScreen(navController = navController)
        }
    }
}


