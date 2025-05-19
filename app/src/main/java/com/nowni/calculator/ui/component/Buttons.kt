package com.nowni.calculator.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nowni.calculator.ui.theme.CalculatorTheme

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
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    elevation: Dp = 10.dp,
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation =elevation,
            pressedElevation = 10.dp / 2,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = text, fontSize = fontSize,
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun NeumorphicButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = Color(0xFF2E2E2E)
    val darkShadow = Color(0xFF202020)
    val lightShadow = Color(0xFF3C3C3C)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(37.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF292929), Color(0xFF313131)),
                    start = Offset(0f, 0f),
                    end = Offset(400f, 400f)
                )
            )
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(37.dp),
                ambientColor = lightShadow,
                spotColor = darkShadow
            )
            .clickable { onClick() }
            .padding(horizontal = 32.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
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

@PreviewLightDark
@Composable
private fun NeuButtonPreview() {
    CalculatorTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {

            Box(modifier = Modifier.padding(20.dp)) {
                NeumorphicButton(
                    text = "Hello",
                    onClick = {},

                    )
            }
        }
    }

}
