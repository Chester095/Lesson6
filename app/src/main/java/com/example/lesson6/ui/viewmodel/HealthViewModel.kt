package com.example.lesson6.ui.viewmodel // ktlint-disable package-name

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lesson6.common.AppState
import com.example.lesson6.data.model.HealthModel
import com.example.lesson6.data.repositories.HealthRepository
import com.example.lesson6.ui.events.MainActivityUiEvent
import com.example.lesson6.ui.sideeffects.MainActivitySideEffects
import com.example.lesson6.ui.state.MainActivityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(private val healthRepository: HealthRepository) :
    ViewModel() {

    private val _state: MutableStateFlow<MainActivityUiState> =
        MutableStateFlow(MainActivityUiState())
    val state: StateFlow<MainActivityUiState> = _state.asStateFlow()

    private val _effect: Channel<MainActivitySideEffects> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        sendEvent(MainActivityUiEvent.GetHealths)
    }

    fun sendEvent(event: MainActivityUiEvent) {
        reduce(oldState = _state.value, event = event)
    }

    private fun setEffect(builder: () -> MainActivitySideEffects) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    private fun setState(newState: MainActivityUiState) {
        _state.value = newState
    }

    private fun reduce(oldState: MainActivityUiState, event: MainActivityUiEvent) {
        when (event) {

            is MainActivityUiEvent.AddHealths -> addTask(
                oldState = oldState,
                upperPressure = event.upperPressure,
                lowerPressure = event.lowerPressure,
                pulse = event.pulse
            )

            is MainActivityUiEvent.DeleteNote -> deleteNote(
                oldState = oldState,
                healthId = event.healthId
            )

            MainActivityUiEvent.GetHealths -> getTasks(oldState)

            is MainActivityUiEvent.OnChangeAddHealthDialogState -> onChangeAddHealthDialog(
                oldState = oldState,
                isShown = event.show
            )

            is MainActivityUiEvent.OnChangeUpperPressure -> onChangeUpperPressure(
                oldState = oldState,
                upperPressure = event.upperPressure
            )

            is MainActivityUiEvent.OnChangeLowerPressure -> onChangeLowerPressure(
                oldState = oldState,
                lowerPressure = event.lowerPressure
            )

            is MainActivityUiEvent.OnChangePulse -> onChangePulse(
                oldState = oldState,
                pulse = event.pulse
            )

            is MainActivityUiEvent.OnChangeUpdateHealthDialogState -> onUpdateAddHealthDialog(
                oldState = oldState,
                isShown = event.show
            )

            is MainActivityUiEvent.SetHealthToBeUpdated -> setHealthToBeUpdated(
                oldState = oldState,
                healthModel = event.healthToBeUpdated
            )

            MainActivityUiEvent.UpdateNote -> updateNote(oldState = oldState)
        }
    }

    private fun addTask(
        oldState: MainActivityUiState,
        upperPressure: String,
        lowerPressure: String,
        pulse: String
    ) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))
            when (val result = healthRepository.addHealth(
                upperPressure = upperPressure,
                lowerPressure = lowerPressure,
                pulse = pulse
            )) {
                is AppState.Failure -> {
                    setState(oldState.copy(isLoading = false))
                    val errorMessage =
                        result.exception.message ?: "An error occurred when adding health"
                    setEffect { MainActivitySideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is AppState.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currentTextFieldUpperPressure = "",
                            currentTextFieldLowerPressure = "",
                            currentTextFieldPulse = "",
                        ),
                    )

                    sendEvent(MainActivityUiEvent.OnChangeAddHealthDialogState(show = false))

                    sendEvent(MainActivityUiEvent.GetHealths)

                    setEffect {
                        MainActivitySideEffects.ShowSnackBarMessage(message = "Health added successfully") }
                }
            }
        }
    }

    private fun getTasks(oldState: MainActivityUiState) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            when (val result = healthRepository.getAllHealth()) {
                is AppState.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your health"
                    setEffect { MainActivitySideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is AppState.Success -> {
                    val health = result.data
                    setState(oldState.copy(isLoading = false, health = health))
                }
            }
        }
    }

    private fun deleteNote(oldState: MainActivityUiState, healthId: String) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            when (val result = healthRepository.deleteHealth(healthId = healthId)) {
                is AppState.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when deleting health"
                    setEffect { MainActivitySideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is AppState.Success -> {
                    setState(oldState.copy(isLoading = false))

                    setEffect { MainActivitySideEffects.ShowSnackBarMessage(message = "Health deleted successfully") }

                    sendEvent(MainActivityUiEvent.GetHealths)
                }
            }
        }
    }

    private fun updateNote(oldState: MainActivityUiState) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            val upperPressure = oldState.currentTextFieldUpperPressure
            val lowerPressure = oldState.currentTextFieldLowerPressure
            val pulse = oldState.currentTextFieldPulse
            val taskToBeUpdated = oldState.healthToBeUpdated

            when (
                val result = healthRepository.updateHealth(
                    upperPressure = upperPressure,
                    lowerPressure = lowerPressure,
                    pulse = pulse,
                    healthId = taskToBeUpdated?.healthId ?: "",
                )
            ) {
                is AppState.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when updating health"
                    setEffect { MainActivitySideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is AppState.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currentTextFieldUpperPressure = "",
                            currentTextFieldLowerPressure = "",
                            currentTextFieldPulse = "",
                        ),
                    )

                    sendEvent(MainActivityUiEvent.OnChangeUpdateHealthDialogState(show = false))

                    setEffect { MainActivitySideEffects.ShowSnackBarMessage(message = "Health updated successfully") }

                    sendEvent(MainActivityUiEvent.GetHealths)
                }
            }
        }
    }

    private fun onChangeAddHealthDialog(oldState: MainActivityUiState, isShown: Boolean) {
        setState(oldState.copy(isShowAddHealthDialog = isShown))
    }

    private fun onUpdateAddHealthDialog(oldState: MainActivityUiState, isShown: Boolean) {
        setState(oldState.copy(isShowUpdateHealthDialog = isShown))
    }

    private fun onChangeUpperPressure(oldState: MainActivityUiState, upperPressure: String) {
        setState(oldState.copy(currentTextFieldUpperPressure = upperPressure))
    }

    private fun onChangeLowerPressure(oldState: MainActivityUiState, lowerPressure: String) {
        setState(oldState.copy(currentTextFieldLowerPressure = lowerPressure))
    }

    private fun onChangePulse(oldState: MainActivityUiState, pulse: String) {
        setState(oldState.copy(currentTextFieldPulse = pulse))
    }

    private fun setHealthToBeUpdated(oldState: MainActivityUiState, healthModel: HealthModel) {
        setState(oldState.copy(healthToBeUpdated = healthModel))
    }
}
