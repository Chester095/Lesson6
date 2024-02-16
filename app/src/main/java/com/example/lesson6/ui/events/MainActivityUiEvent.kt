package com.example.lesson6.ui.events

import com.example.lesson6.data.model.HealthModel

sealed class MainActivityUiEvent {
    data object GetHealths : MainActivityUiEvent()
    data class AddHealths(val upperPressure: String, val lowerPressure: String, val pulse: String) :
        MainActivityUiEvent()
    data object UpdateNote : MainActivityUiEvent()
    data class DeleteNote(val healthId: String) : MainActivityUiEvent()
    data class OnChangeUpperPressure(val upperPressure: String) : MainActivityUiEvent()
    data class OnChangeLowerPressure(val lowerPressure: String) : MainActivityUiEvent()
    data class OnChangePulse(val pulse: String) : MainActivityUiEvent()
    data class OnChangeAddHealthDialogState(val show: Boolean) : MainActivityUiEvent()
    data class OnChangeUpdateHealthDialogState(val show: Boolean) :
        MainActivityUiEvent()
    data class SetHealthToBeUpdated(val healthToBeUpdated: HealthModel) : MainActivityUiEvent()
}