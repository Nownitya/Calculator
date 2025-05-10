package com.nowni.calculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nowni.calculator.ui.component.ArithmeticButton
import com.nowni.calculator.ui.component.NumberButton

/**
 * Composable function that represents the main screen of the calculator application.
 * It displays the calculator display and the calculator buttons.
 * It also handles the logic for button clicks and updates the display accordingly.
 *
 * @param modifier The modifier to be applied to the composable.
 */
@Composable
fun CalculatorHome(modifier: Modifier = Modifier) {

    var displayText by remember { mutableStateOf("0") }
    val operators = listOf("+", "-", "*", "/", "%")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        CalculatorDisplay(
            text = displayText, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Box(
            contentAlignment = Alignment.BottomCenter, modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            CalculatorButtons(
                onButtonClick = { btn ->
                    displayText = when (btn) {
                        "AC" -> "0"
                        "DEL" -> displayText.dropLast(1).ifEmpty { "0" }
                        "=" -> {
                            val lastChar = displayText.lastOrNull()
                            if (lastChar in operators.map { it[0] } || lastChar == '.') {
                                displayText
                            } else {
                                try {
                                    val result = evaluate(displayText)
                                    if (result == result.toLong().toDouble()) result.toLong()
                                        .toString() else result.toString()


                                } catch (e: Exception) {
                                    "Error"
                                }
                            }

                        }

                        "." -> {
                            val segments = displayText.split(*operators.toTypedArray().map { it[0] }
                                .toCharArray())
                            if (!segments.last().contains(".")) {
                                "$displayText."
                            } else {
                                displayText
                            }
                        }/*in operators -> {
                            val lastChar = displayText.lastOrNull()
                            if (lastChar!= null && lastChar !in operators.map { it[0] } && lastChar !='.') displayText+btn else displayText

                        }*/
                        else -> if (displayText == "0") btn else displayText + btn
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
        }
    }

}


/**
 * Displays the current text on the calculator screen.
 *
 * This composable shows the input or the result of calculations.
 * The font size of the displayed text adjusts based on its length to fit within the display area.
 * If the text is "Error", it is displayed in the error color.
 *
 * @param text The text to display on the calculator screen.
 * @param modifier [Modifier] to be applied to the layout of the display.
 */
@Composable
fun CalculatorDisplay(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            text = text,
            fontSize = when {
                text.length > 10 -> 36.sp
                text.length > 8 -> 42.sp
                else -> 48.sp
            },
            color = if (text == "Error") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.displayMedium

        )
    }
}

/**
 * Composable function that displays the calculator buttons.
 *
 * @param onButtonClick Lambda function to be invoked when a button is clicked.
 * @param modifier Optional [Modifier] for customizing the layout of the button grid.
 */
@Composable
fun CalculatorButtons(
    onButtonClick: (String) -> Unit, modifier: Modifier = Modifier
) {
    val keys = listOf(
        listOf("AC", "DEL", "%", "/"),
        listOf("7", "8", "9", "*"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp), content = {
            keys.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        row.forEach { label ->
                            val weightVal = if (label == "=") 2f else 1f
                            val btnModifier =
                                Modifier
                                    .weight(weightVal)
                                    .aspectRatio(if (label == "=") 2f else 1f)
                            when (label) {
                                "AC", "DEL", "+", "-", "*", "/", "%", ".", "=" -> ArithmeticButton(
                                    onclick = { onButtonClick(label) },
                                    text = label,
                                    modifier = btnModifier
                                )

                                else -> NumberButton(
                                    onclick = { onButtonClick(label) },
                                    text = label.toIntOrNull() ?: 0,
                                    modifier = btnModifier
                                )

                            }


                        }
                    })
            }
        })

}


/**
 * Evaluates a mathematical expression represented as a string.
 * This function supports basic arithmetic operations: addition (+), subtraction (-),
 * multiplication (*), division (/), and modulo (%). It handles positive and negative numbers,
 * as well as decimal values.
 *
 * The evaluation follows the standard order of operations (multiplication, division, modulo
 * before addition and subtraction).
 *
 * @param expr The mathematical expression string to evaluate.
 * @return The result of the evaluation as a Double.
 * @throws NumberFormatException if the input string contains invalid number formats.
 * @throws ArithmeticException if a division by zero occurs.
 */
fun evaluate(expr: String): Double {
    var index = 0

    fun parseNumber(): Double {
        val start = index

        if (expr.getOrNull(index) == '-') {
            index++
        }
        while (expr.getOrNull(index)?.let { it.isDigit() || it == '.' } == true) {
            index++
        }
        return expr.substring(start, index).toDouble()

    }

    fun parseTerm(): Double {
        var value = parseNumber()
        while (index < expr.length) {
            when (expr[index]) {
                '*' -> {
                    index++
                    value *= parseNumber()
                }

                '/' -> {
                    index++
                    value /= parseNumber()
                }

                '%' -> {
                    index++
                    value %= parseNumber()
                }

                else -> break
            }
        }
        return value

    }

    fun parseExpression(): Double {
        var value = parseTerm()
        while (index < expr.length) {
            when (expr[index]) {
                '+' -> {
                    index++
                    value += parseTerm()
                }

                '-' -> {
                    index++
                    value -= parseTerm()
                }

                else -> break
            }
        }
        return value

    }
    return parseExpression()
}
