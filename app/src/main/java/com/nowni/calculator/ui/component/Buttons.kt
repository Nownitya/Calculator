package com.nowni.calculator.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nowni.calculator.ui.theme.CalculatorTheme

//import androidx.compose.runtime.getValue


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
//@Composable
//fun CalculatorButton(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    fontSize: TextUnit = 20.sp,
//    color: Color = MaterialTheme.colorScheme.secondaryContainer,
//    elevation: Dp = 10.dp,
//) {
//    Button(
//        onClick = { onClick() },
//        modifier = modifier,
//        elevation = ButtonDefaults.buttonElevation(
//            defaultElevation = elevation,
//            pressedElevation = 10.dp / 2,
//            disabledElevation = 0.dp
//        ),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = color,
//            contentColor = MaterialTheme.colorScheme.onPrimary
//        ),
//        shape = MaterialTheme.shapes.large
//    ) {
//        Text(
//            text = text, fontSize = fontSize,
//            modifier = Modifier.padding(2.dp)
//        )
//    }
//}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    elevation: Dp = 10.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val darkShadow = Color(0xFF202020)
    val lightShadow = Color(0xFF3C3C3C)
    Button(
        onClick = { onClick() },
        modifier = modifier
            .shadow(
                elevation = if (isPressed) 0.dp else elevation,
                MaterialTheme.shapes.large,
                clip = false,
                ambientColor = lightShadow,
                spotColor = darkShadow
            ),
        elevation= ButtonDefaults.buttonElevation(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color, contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.large,
        interactionSource = interactionSource
    ) {
        Text(
            text = text, fontSize = fontSize, modifier = Modifier.padding(2.dp)
        )
    }
}



@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ButtonPreview() {
    CalculatorTheme {
        CalculatorButton(
            text = "Hello",
            onClick = {},
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }

}
