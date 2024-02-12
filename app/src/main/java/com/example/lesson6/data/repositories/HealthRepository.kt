package com.example.lesson6.data.repositories

import com.example.lesson6.data.model.HealthModel
import com.example.lesson6.common.AppState

interface HealthRepository {
    suspend fun addHealth(upperPressure: String, lowerPressure: String, pulse: String): AppState<Unit>
    suspend fun getAllHealth(): AppState<List<HealthModel>>
    suspend fun deleteHealth(healthId: String): AppState<Unit>
    suspend fun updateHealth(
        healthId: String,
        upperPressure: String,
        lowerPressure: String,
        pulse: String
    ): AppState<Unit>
}