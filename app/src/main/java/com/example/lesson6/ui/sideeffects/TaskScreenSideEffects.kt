package com.example.lesson6.ui.sideeffects

sealed class MainActivitySideEffects {
    data class ShowSnackBarMessage(val message: String) : MainActivitySideEffects()
}
