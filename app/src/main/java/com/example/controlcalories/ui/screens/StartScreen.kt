package com.example.controlcalories.ui.screens

import android.widget.Button
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.controlcalories.R
import com.example.controlcalories.ui.default_components.Button
import com.example.controlcalories.ui.default_components.ButtonPreview
import com.example.controlcalories.ui.theme.Typography
import com.example.controlcalories.ui.theme.defaultCorrectColor
import com.example.controlcalories.ui.theme.defaultIncorrectColor
import com.example.controlcalories.ui.theme.defaultStartScreenColor
import com.example.controlcalories.ui.theme.defaultTextColor
import kotlin.math.round

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onNewUser: () -> Unit = {},
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
                    onClick = onNewUser
                )
                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(280.dp, 60.dp),
                    text = "Utrzymanie masy ciała",
                    textStyle = Typography.labelLarge,
                    onClick = onNewUser
                )
                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(280.dp, 60.dp),
                    text = "Przyrost masy ciała",
                    textStyle = Typography.labelLarge,
                    onClick = onNewUser
                )
            }
        }
    }


}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    StartScreen()
}
