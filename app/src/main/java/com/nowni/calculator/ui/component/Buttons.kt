package com.nowni.calculator.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function that displays a button with a number as its text.
 *
 * @param onclick The lambda function to execute when the button is clicked.
 * @param modifier Optional [Modifier] for the button.
 * @param text The integer number to display on the button. This will be converted to a string.
 */
@Composable
fun NumberButton(
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
    text: Int,

    ) {
    Button(
        onClick = onclick,
        modifier = modifier
    ) {
        Text(text = text.toString())
    }

}

/**
 * A composable function that represents a button for arithmetic operations.
 *
 * @param onclick The lambda function to be executed when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 * @param text The text to be displayed on the button.
 */
@Composable
fun ArithmeticButton(
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
) {
    Button(
        onClick = onclick,
        modifier = modifier
    ) {
        Text(text = text, fontSize = 16.sp)
    }

}


@Preview
@Composable
private fun ButtonPreview() {

    Column(
        modifier = Modifier
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            NumberButton(
                onclick = {}, text = 1, modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
            ArithmeticButton(
                onclick = {}, text = "AC", modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    )

}

@Preview
@Composable
private fun Prev() {
    ArithmeticButton(
        onclick = {}, text = "AC", modifier = Modifier
            .fillMaxSize()

    )


}