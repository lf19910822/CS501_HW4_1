package com.example.hw4_1

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LifecycleEvent(
    val eventName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val color: Color = getColorForEvent(eventName)
) {
    fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

fun getColorForEvent(eventName: String): Color {
    return when (eventName) {
        "onCreate" -> Color(0xFF4CAF50)      // Green
        "onStart" -> Color(0xFF8BC34A)       // Light Green
        "onResume" -> Color(0xFF2196F3)      // Blue
        "onPause" -> Color(0xFFFFC107)       // Amber
        "onStop" -> Color(0xFFFF9800)        // Orange
        "onDestroy" -> Color(0xFFF44336)     // Red
        "onRestart" -> Color(0xFF9C27B0)     // Purple
        else -> Color.Gray
    }
}

