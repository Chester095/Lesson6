package com.example.lesson6.common

sealed class AppState<out T : Any> {
    data class Success<out T : Any>(val data: T) : AppState<T>()
    data class Failure(val exception: Exception) : AppState<Nothing>()
}
