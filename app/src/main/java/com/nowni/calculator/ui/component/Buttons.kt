package com.nowni.calculator.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 20.sp
) {
    Button(
        onClick = { onClick()},
        modifier = modifier
    ) {
        Text(text = text, fontSize = fontSize)
    }
}
