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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nowni.calculator.ui.component.CalculatorButton


@Composable
fun CalculatorHome(modifier: Modifier = Modifier) {

    var displayText by remember { mutableStateOf("0") }
    val operators = listOf("+", "-", "*", "/", "%")

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
                displayText = when (btn) {
                    "AC" -> "0"
                    "DEL" -> if (displayText == "Error") "0" else displayText.dropLast(1)
                        .ifEmpty { "0" }

                    "=" -> calculateResult(displayText, operators)
                    else -> handleInput(btn, displayText, operators)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
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
            .heightIn(min = 80.dp),
        contentAlignment = Alignment.CenterEnd

    ) {
        Text(
            text = text,
            fontSize = when {
                text.length > 10 -> 36.sp
                text.length > 8 -> 42.sp
                else -> 48.sp
            },
            color = if (text.equals(
                    "error",
                    ignoreCase = true
                )
            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

fun calculateResult(expr: String, operators: List<String>): String {
    return if (expr.lastOrNull() in operators.map { it[0] } || expr.lastOrNull() == '.') {
        expr
    } else {
        try {
            val result = evaluate(expr)
            if (result % 1 == 0.0) {
                result.toLong().toString()
            } else {
                result.toString()
            }
        } catch (_: Exception) {
            "Error"
        }
    }
}


fun handleInput(btn: String, current: String, operators: List<String>): String {
    return when {
        current == "Error" -> handleErrorState(btn)
        btn == "." -> handleDecimal(current)
        btn in operators -> handleOperator(btn, current)
        else -> handleNumber(btn, current)
    }

}

private fun handleErrorState(btn: String) = when (btn) {
    "." -> "0."
    in listOf("+", "-", "/", "%") -> "0"
    "-" -> "-"
    else -> btn
}


private fun handleDecimal(current: String): String {

    val lastOperatorIndex = current.indexOfLast { it in "+-*/%" }
    val currentNumber =
        if (lastOperatorIndex == -1) current else current.substring(lastOperatorIndex + 1)
    return when {
        current == "0" -> "0."
        currentNumber.isEmpty() -> "${current}0."
        currentNumber.contains('.') -> current
        else -> "${current}."
    }
}

private fun handleOperator(btn: String, current: String): String {
    if (current == "0") {
        return if (btn == "-") "-" else "0"
    }

    val lastChar = current.lastOrNull()
    val length = current.length

    // Check for operator followed by '-'
    if (length >= 2) {
        val secondLastChar = current[length - 2]
        if (lastChar == '-' && secondLastChar in "+*/%") {
            return when {
                btn == "-" -> current // Prevent triple operators
                else -> current.dropLast(2) + btn
            }
        }
    }

    return when {
        lastChar != null && lastChar in "+*/%" -> {
            when (btn) {
                "-" -> "$current-"
                else -> current.dropLast(1) + btn
            }
        }

        lastChar == '-' -> {
            if (length >= 2 && current[length - 2] in "+*/%") {
                // Replace operator followed by '-'
                current.dropLast(2) + btn
            } else {
                // Regular subtraction replacement
                current.dropLast(1) + btn
            }
        }

        else -> "$current$btn"
    }
}

private fun handleNumber(btn: String, current: String) =
    if (current == "0") btn else "${current}${btn}"

@Composable
fun CalculatorButtonPanel(
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keys = listOf(
        listOf("AC", "DEL", "%", "/"),
        listOf("7", "8", "9", "*"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { label ->
                    val weight = when (label) {
                        "=" -> 2f
                        else -> 1f
                    }
                    CalculatorButton(
                        onClick = { onButtonClick(label) },
                        modifier = Modifier
                            .weight(weight)
                            .aspectRatio(if (label == "=") 2f else 1f),
                        text = label,
                        fontSize = if (label in listOf(
                                "AC", "DEL", "%", "/", "*", "-", "+", "="
                            )
                        ) 16.sp else 20.sp

                    )
                }
            }
        }
    }
}


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

@Preview
@Composable
private fun CalculatorHomePreview() {
    MaterialTheme {
        Surface {
            CalculatorHome()
        }
    }

}
