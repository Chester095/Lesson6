package com.example.lesson6.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.lesson6.ui.state.MainActivityUiState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHealthDialogComponent(
    setUpperPressure: (String) -> Unit,
    setLowerPressure: (String) -> Unit,
    setPulse: (String) -> Unit,
    saveTask: () -> Unit,
    closeDialog: () -> Unit,
    uiState: MainActivityUiState,
) {
    Dialog(onDismissRequest = { closeDialog() }) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyColumn(contentPadding = PaddingValues(12.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Новая запись",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.currentTextFieldUpperPressure,
                        onValueChange = { upperPressure ->
                            Timber.tag("!!!").d("AddHealthDialogComponent upperPressure = $upperPressure")
                            setUpperPressure(upperPressure)
                        },
                        label = { Text("Верхнее давление") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = Color.Black,
                        ),
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.currentTextFieldLowerPressure,
                        onValueChange = { lowerPressure ->
                            Timber.tag("!!!").d("AddHealthDialogComponent lowerPressure = $lowerPressure")
                            setLowerPressure(lowerPressure)
                        },
                        label = { Text("Нижнее давление") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = Color.Black,
                        ),
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.currentTextFieldPulse,
                        onValueChange = { pulse ->
                            Timber.tag("!!!").d("pulse = $pulse")
                            setPulse(pulse)
                        },
                        label = { Text("Пульс") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = Color.Black,
                        ),
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = {
                                Timber.tag("!!!").d("Button onClick = true")
                                Timber.tag("!!!").d("Unit = ${Unit}")
                                saveTask()
                                setUpperPressure("")
                                setLowerPressure("")
                                setPulse("")
                                closeDialog()
                            },
                            modifier = Modifier.padding(horizontal = 12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White,
                            ),
                        ) {
                            Text(text = "Сохранить")
                        }
                    }
                }
            }
        }
    }
}
