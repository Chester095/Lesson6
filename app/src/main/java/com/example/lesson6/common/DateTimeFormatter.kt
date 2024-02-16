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
    val newDateFormat = SimpleDateFormat("HH:mm", Locale("ru"))
    val date = currentDateFormat.parse(dateString)
    return newDateFormat.format(date)
}

fun convertDateFormatToDateMonth(dateString: String): String {
    val currentDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("dd MMMM", Locale("ru"))
    val date = currentDateFormat.parse(dateString)
    return newDateFormat.format(date)

}fun convertDateFormatToYearMonthDate(dateString: String): String {
    val currentDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    val date = currentDateFormat.parse(dateString)
    return newDateFormat.format(date)
}
