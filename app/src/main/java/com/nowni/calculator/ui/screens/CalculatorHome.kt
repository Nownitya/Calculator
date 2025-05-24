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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nowni.calculator.ui.component.CalculatorButton
import com.nowni.calculator.ui.theme.CalculatorTheme
import net.objecthunter.exp4j.ExpressionBuilder


@Composable
fun CalculatorHome(modifier: Modifier = Modifier) {

    var displayText by remember { mutableStateOf("0") }
    var isResultDisplayed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CalculatorDisplay(
            text = displayText, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        CalculatorButtonPanel(
            onButtonClick = { btn ->
                when (btn) {
                    "AC/DEL" -> handleClear(displayText, isResultDisplayed) { newText, newState ->
                        displayText = newText
                        isResultDisplayed = newState
                    }

                    "=" -> {
                        displayText = calculateResult(displayText)
                        isResultDisplayed = true
                    }

                    "+/-" -> {
                        displayText = toggleSign(displayText)
                        isResultDisplayed = false
                    }

                    else -> {
                        if (isResultDisplayed && btn !in listOf("+", "-", "×", "÷", "%")) {
                            displayText = "0"
                            isResultDisplayed = false
                        }
                        displayText = handleInput(btn, displayText)
                        isResultDisplayed = false
                    }
                }
            }, showAC = isResultDisplayed || displayText == "0"
        )

    }
}


@Composable
fun CalculatorDisplay(
    text: String, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .heightIn(min = 80.dp), contentAlignment = Alignment.CenterEnd

    ) {
        Text(
            text = text,
            fontSize = when {
                text.length > 10 -> 36.sp
                text.length > 8 -> 42.sp
                else -> 48.sp
            },
            color = if (text.equals(
                    "error", ignoreCase = true
                )
            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
fun CalculatorButtonPanel(
    onButtonClick: (String) -> Unit, modifier: Modifier = Modifier, showAC: Boolean
) {
    val keys = listOf(
        listOf("AC/DEL", "+/-", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { label ->
                    val displayLabel = when {
                        label == "AC/DEL" && showAC -> "AC"
                        label == "AC/DEL" -> "DEL"
                        else -> label
                    }

                    val weight = when (label) {
                        "0" -> 2f
                        else -> 1f
                    }

                    CalculatorButton(
                        text = displayLabel,
                        onClick = { onButtonClick(label) },
                        modifier = Modifier
                            .weight(weight)
                            .aspectRatio(if (label == "0") 2f else 1f),
                        fontSize = when {
                            displayLabel in listOf("AC", "DEL", "%", "÷", "×") -> 20.sp
                            else -> 24.sp
                        },
                        color = when (displayLabel) {
                            "AC" -> MaterialTheme.colorScheme.errorContainer
                            in listOf(
                                "÷", "×", "-", "+", "%", "=", "+/-"
                            ) -> MaterialTheme.colorScheme.primary

                            else -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                }
            }

        }
    }
}

private fun handleClear(
    current: String, isResult: Boolean, onStateChange: (String, Boolean) -> Unit
) {
    if (isResult || current == "Error") {
        onStateChange("0", false)
    } else {
        val newText = current.dropLast(1).ifEmpty { "0" }
        onStateChange(newText, newText == "0")
    }
}

private fun toggleSign(current: String): String {
    return when {
        current == "Error" -> "-"
        current.isEmpty() || current == "0" -> "-"
        current == "-" -> "0"

        else -> {
            val lastNumberRange = findLastNumberRange(current)
            if (lastNumberRange != null) {
                toggleNumberExpression(current, lastNumberRange)
            } else {
                "$current-"
            }
        }
    }.let { result ->
        result.ifEmpty { "0" }
    }
}

private fun findLastNumberRange(expression: String): IntRange? {
    if (expression.isEmpty()) {
        return null
    }

    // 1. Find the end of the last number (skip trailing operators)
    val endIndex = expression.indexOfLast { char ->
        // A character is part of a number if it's a digit or a decimal point.
        char.isDigit() || char == '.'
    }

    // If no digit or decimal point is found, there's no number.
    if (endIndex == -1) {
        return null
    }

    // 2. Find the start of the number ending at 'endIndex'
    var startIndex = endIndex
    while (startIndex >= 0) {
        val currentChar = expression[startIndex]
        val previousChar = expression.getOrNull(startIndex - 1)

        if (currentChar.isDigit() || currentChar == '.') {
            startIndex--
        } else if (currentChar == '-' && (
                    startIndex == 0 ||
                            previousChar == null ||
                            previousChar in "+-×÷%"
                    )) {
            // Unary minus part of number
            startIndex--
            break
        } else {
            break
        }
    }
    startIndex++  // adjust to actual start

    return if (startIndex <= endIndex) {
        startIndex..endIndex
    } else {
        null
    }
}

private fun toggleNumberExpression(expression: String, range: IntRange): String {
    val number = expression.substring(range)
    val toggledNumber = if (number.startsWith("-")) number.drop(1) else "-$number"

    return buildString {
        append(expression.substring(0, range.first))
        append(toggledNumber)
        append(expression.substring(range.last + 1))
    }
}

private fun handleInput(btn: String, current: String): String {
    return when {
        current == "Error" -> btn
        btn == "." -> handleDecimal(current)
        btn in listOf("+", "-", "×", "÷", "%") -> handleOperator(btn, current)
        else -> handleNumberOrParenthesis(btn, current)
    }
}

private fun handleDecimal(current: String): String {
    val parts = current.split(Regex("[-+×÷%]"))
    return if (parts.lastOrNull()?.contains('.') == true) current else "$current."
}

private fun handleOperator(btn: String, current: String): String {
//    val operators = setOf("+", "-", "×", "÷", "%")
    val operators = setOf('+', '-', '×', '÷', '%')
    return when {
        current == "0" && btn == "-" -> "-"
        current.isNotEmpty() && current.last() in operators -> current.dropLast(1) + btn
        else -> "$current$btn"
    }
}

private fun handleNumberOrParenthesis(btn: String, current: String): String {
    return when {
        current == "0" && btn != "." -> btn
        else -> "$current$btn"
    }
}

fun calculateResult(expression: String): String {
    return try {
        val sanitizeExpr = expression.replace("÷", "/").replace("×", "*").replace("%", "/100")
        val result = ExpressionBuilder(sanitizeExpr).build().evaluate()

        if (result % 1 == 0.0) {
            result.toLong().toString()
        } else {
            "0.6f".format(result).trimEnd('0').trimEnd('.')
        }
    } catch (_: Exception) {
        "Error"
    }
}

@PreviewLightDark
@Composable
private fun ButtonPanelPrev() {
    CalculatorTheme {
        Surface {
            CalculatorButtonPanel(onButtonClick = {}, showAC = true)
        }
    }

}

