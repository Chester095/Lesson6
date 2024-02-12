package com.example.lesson6.ui.state

import com.example.lesson6.data.model.HealthModel

data class MainActivityUiState(
    val isLoading: Boolean = false,
    val health: List<HealthModel> = emptyList(),
    val errorMessage: String? = null,
    val healthToBeUpdated: HealthModel? = null,
    val isShowAddHealthDialog: Boolean = false,
    val isShowUpdateHealthDialog: Boolean = false,
    val currentTextFieldUpperPressure: String = "",
    val currentTextFieldLowerPressure: String = "",
    val currentTextFieldPulse: String = "",
)
