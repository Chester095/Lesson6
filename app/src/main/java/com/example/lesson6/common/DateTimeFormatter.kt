package com.example.lesson6.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentTimeAsString(): String {
    val currentTime = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(currentTime)
}

fun convertDateFormat(dateString: String): String {
    val currentDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("EEEE MMMM yyyy, h:mma", Locale.getDefault())
    val date = currentDateFormat.parse(dateString)
    return newDateFormat.format(date)
}
