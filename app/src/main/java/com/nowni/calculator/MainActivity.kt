package com.nowni.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.nowni.calculator.ui.screens.CalculatorHome
import com.nowni.calculator.ui.theme.CalculatorTheme

/**
 * The main activity for the Calculator application.
 *
 * This activity sets up the main screen of the application using Jetpack Compose.
 * It utilizes a [Scaffold] to provide basic Material Design structure and displays the [CalculatorHome] composable.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            CalculatorHome()
                        }
                    }
                )
            }
        }
    }
}


