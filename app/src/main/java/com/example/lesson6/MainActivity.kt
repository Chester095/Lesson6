package com.example.lesson6 // ktlint-disable filename

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lesson6.common.SIDE_EFFECTS_KEY
import com.example.lesson6.ui.components.AddHealthDialogComponent
import com.example.lesson6.ui.components.EmptyComponent
import com.example.lesson6.ui.components.ItemColumn
import com.example.lesson6.ui.components.LoadingComponent
import com.example.lesson6.ui.components.UpdateHealthDialogComponent
import com.example.lesson6.ui.events.MainActivityUiEvent
import com.example.lesson6.ui.sideeffects.MainActivitySideEffects
import com.example.lesson6.ui.theme.Lesson6Theme
import com.example.lesson6.ui.viewmodel.HealthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val healthViewModel: HealthViewModel = viewModel()

            val uiState = healthViewModel.state.collectAsState().value
            val effectFlow = healthViewModel.effect

            val snackBarHostState = remember { SnackbarHostState() }

            LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
                effectFlow.onEach { effect ->
                    when (effect) {
                        is MainActivitySideEffects.ShowSnackBarMessage -> {
                            snackBarHostState.showSnackbar(
                                message = effect.message,
                                duration = SnackbarDuration.Short,
                                actionLabel = "DISMISS",
                            )
                        }
                    }
                }.collect()
            }

            Lesson6Theme {
                if (uiState.isShowAddHealthDialog) {
                    AddHealthDialogComponent(
                        uiState = uiState,
                        setUpperPressure = { upperPressure ->
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeUpperPressure(
                                    upperPressure = upperPressure
                                )
                            )
                        },
                        setLowerPressure = { lowerPressure ->
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeLowerPressure(
                                    lowerPressure = lowerPressure
                                )
                            )
                        },
                        setPulse = { pulse ->
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangePulse(pulse = pulse)
                            )
                        },
                        saveTask = {
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.AddHealths(
                                    lowerPressure = uiState.currentTextFieldLowerPressure,
                                    upperPressure = uiState.currentTextFieldLowerPressure,
                                    pulse = uiState.currentTextFieldPulse
                                )
                            )
                        },
                        closeDialog = {
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeAddHealthDialogState(show = false)
                            )
                        }
                    )
                }

                if (uiState.isShowUpdateHealthDialog) {
                    UpdateHealthDialogComponent(
                        uiState = uiState,
                        setUpperPressure = { upperPressure ->
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeUpperPressure(
                                    upperPressure = upperPressure
                                )
                            )
                        },
                        setLowerPressure = { lowerPressure ->
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeLowerPressure(
                                    lowerPressure = lowerPressure
                                )
                            )
                        },
                        setPulse = { pulse ->
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangePulse(pulse = pulse)
                            )
                        },
                        saveTask = {
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.UpdateNote
                            )
                        },
                        closeDialog = {
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeUpdateHealthDialogState(show = false)
                            )
                        },
                        healthModel = uiState.healthToBeUpdated,
                    )
                }

                Scaffold(
                    floatingActionButton = {
                        Column {
                            FloatingActionButton(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                shape = CircleShape,
                                onClick = {
                                    healthViewModel.sendEvent(
                                        event = MainActivityUiEvent.OnChangeAddHealthDialogState(
                                            show = true
                                        ),
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Add Button"
                                )
                            }
                        }
                    }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        when {
                            uiState.isLoading -> {
                                LoadingComponent()
                            }

                            !uiState.isLoading && uiState.health.isNotEmpty() -> {
                                LazyColumn {
                                    items(uiState.health) { health ->
                                        ItemColumn(healthModel = health)
                                    }
                                }
                            }

                            !uiState.isLoading && uiState.health.isEmpty() -> {
                                EmptyComponent()
                            }
                        }
                    }
                }
            }
        }
    }
}
