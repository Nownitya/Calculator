package com.nowni.calculator.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * A composable function that represents a button used in a calculator.
 *
 * This button displays a given text and triggers an action when clicked.
 *
 * @param onClick The lambda function to be executed when the button is clicked.
 * @param modifier A [Modifier] to be applied to the button. Defaults to [Modifier].
 * @param text The text to be displayed on the button.
 * @param fontSize The size of the text on the button. Defaults to 20.sp.
 */
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
