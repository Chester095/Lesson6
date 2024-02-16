package com.example.lesson6.ui // ktlint-disable filename

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
import com.example.lesson6.ui.components.ItemChoose
import com.example.lesson6.ui.components.LoadingComponent
import com.example.lesson6.ui.components.UpdateHealthDialogComponent
import com.example.lesson6.ui.events.MainActivityUiEvent
import com.example.lesson6.ui.sideeffects.MainActivitySideEffects
import com.example.lesson6.ui.theme.Lesson6Theme
import com.example.lesson6.ui.viewmodel.HealthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

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
                Timber.tag("!!!").d("uiState.Lesson6Theme = true")
                if (uiState.isShowAddHealthDialog) {
                    Timber.tag("!!!")
                        .d("uiState.isShowAddHealthDialog = ${uiState.isShowAddHealthDialog}")
                    AddHealthDialogComponent(
                        uiState = uiState,
                        setUpperPressure = { upperPressure ->
                            Timber.tag("!!!")
                                .d("uiState.isShowAddHealthDialog upperPressure = $upperPressure")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeUpperPressure(
                                    upperPressure = upperPressure
                                )
                            )
                        },
                        setLowerPressure = { lowerPressure ->
                            Timber.tag("!!!").d("lowerPressure = $lowerPressure")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeLowerPressure(
                                    lowerPressure = lowerPressure
                                )
                            )
                        },
                        setPulse = { pulse ->
                            Timber.tag("!!!").d("pulse = $pulse")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangePulse(
                                    pulse = pulse
                                )
                            )
                        },
                        saveTask = {
                            Timber.tag("!!!").d("saveTask = true")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.AddHealths(
                                    upperPressure = uiState.currentTextFieldUpperPressure,
                                    lowerPressure = uiState.currentTextFieldLowerPressure,
                                    pulse = uiState.currentTextFieldPulse
                                )
                            )
                        },
                        closeDialog = {
                            Timber.d("closeDialog = true")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeAddHealthDialogState(show = false)
                            )
                        }
                    )
                }

                if (uiState.isShowUpdateHealthDialog) {
                    Timber.tag("!!!").d("uiState.isShowUpdateHealthDialog = true")
                    UpdateHealthDialogComponent(
                        uiState = uiState,
                        setUpperPressure = { upperPressure ->
                            Timber.tag("!!!")
                                .d("uiState.isShowUpdateHealthDialog upperPressure = ${uiState.isShowAddHealthDialog}")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeUpperPressure(
                                    upperPressure = upperPressure
                                )
                            )
                        },
                        setLowerPressure = { lowerPressure ->
                            Timber.tag("!!!").d("lowerPressure = $lowerPressure")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeLowerPressure(
                                    lowerPressure = lowerPressure
                                )
                            )
                        },
                        setPulse = { pulse ->
                            Timber.tag("!!!").d("pulse = $pulse")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangePulse(pulse = pulse)
                            )
                        },
                        saveTask = {
                            Timber.tag("!!!").d("saveTask = true")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.UpdateNote
                            )
                        },
                        closeDialog = {
                            Timber.tag("!!!").d("closeDialog = closeDialog")
                            healthViewModel.sendEvent(
                                event = MainActivityUiEvent.OnChangeUpdateHealthDialogState(show = false)
                            )
                        },
                        healthModel = uiState.healthToBeUpdated,
                    )
                }

                Scaffold(
                    floatingActionButton = {
                        Timber.tag("!!!").d("floatingActionButton = true")
                        Column {
                            FloatingActionButton(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                shape = CircleShape,
                                onClick = {
                                    Timber.tag("!!!").d("FloatingActionButton.onClick = true")
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
                        Timber.tag("!!!").d("Box = true")
//                        Timber.tag("!!!").d("uiState.isLoading = ${uiState.isLoading}")
                        when {
                            uiState.isLoading -> {
                                Timber.tag("!!!").d("uiState.isLoading = true")
                                LoadingComponent()
                            }

                            !uiState.isLoading && uiState.health.isNotEmpty() -> {
                                Timber.tag("!!!")
                                    .d("!uiState.isLoading && uiState.health.isNotEmpty() = true")
                                LazyColumn {
                                    items(uiState.health) { health ->
                                        ItemChoose(healthModel = health)
                                    }
                                }
                            }

                            !uiState.isLoading && uiState.health.isEmpty() -> {
                                Timber.tag("!!!")
                                    .d("!uiState.isLoading && uiState.health.isEmpty() = true")
                                EmptyComponent()
                            }
                        }
                    }
                }
            }
        }
    }
}
