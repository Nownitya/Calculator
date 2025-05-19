package com.nowni.calculator.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes: Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)



object NeumorphicShapes{
    val Button = RoundedCornerShape(16.dp)
    // Display area with inset look
    val Display = RoundedCornerShape(12.dp)
    // Pressed state shape (slightly smaller radius)
    val Pressed = RoundedCornerShape(14.dp)
    // Extended width buttons (0 and =)
    val WideButton = RoundedCornerShape(16.dp)
}
