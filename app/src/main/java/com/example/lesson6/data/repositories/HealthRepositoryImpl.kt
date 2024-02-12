package com.example.lesson6.data.repositories

import com.example.lesson6.common.AppState
import com.example.lesson6.common.COLLECTION_PATH_NAME
import com.example.lesson6.common.PLEASE_CHECK_INTERNET_CONNECTION
import com.example.lesson6.common.convertDateFormat
import com.example.lesson6.common.getCurrentTimeAsString
import com.example.lesson6.data.model.HealthModel
import com.example.lesson6.di.IoDispatcher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

class HealthRepositoryImpl @Inject constructor(
    private val appLesson6Db: FirebaseFirestore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : HealthRepository {

    override suspend fun addHealth(
        upperPressure: String, lowerPressure: String, pulse: String
    ): AppState<Unit> {
        return try {
            withContext(ioDispatcher) {
                val health = hashMapOf(
                    "upperPressure" to upperPressure,
                    "lowerPressure" to lowerPressure,
                    "pulse" to pulse,
                    "createdAt" to getCurrentTimeAsString()
                )

                val addHealthTimeout = withTimeoutOrNull(10000L) {
                    appLesson6Db.collection(COLLECTION_PATH_NAME)
                        .add(health)

                }

                if (addHealthTimeout == null) {
                    AppState.Failure(
                        IllegalStateException(
                            PLEASE_CHECK_INTERNET_CONNECTION
                        )
                    )
                    AppState.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }
                AppState.Success(Unit)
            }
        } catch (exception: Exception) {
            Timber.tag("ERROR: ").d(exception)
            AppState.Failure(exception = exception)
        }
    }

    override suspend fun getAllHealth(): AppState<List<HealthModel>> {
        return try {
            withContext(ioDispatcher) {
                val fetchingHealthTimeout = withTimeoutOrNull(10000L) {
                    appLesson6Db.collection(COLLECTION_PATH_NAME)
                        .get()
                        .await()
                        .documents.map { document ->
                            HealthModel(
                                healthId = document.id,
                                upperPressure = document.getString("upperPressure") ?: "",
                                lowerPressure = document.getString("lowerPressure") ?: "",
                                pulse = document.getString("pulse") ?: "",
                                createdAt = convertDateFormat(
                                    document.getString("createdAt") ?: "",
                                ),
                            )
                        }
                }
                if (fetchingHealthTimeout == null) {
                    Timber.tag("ERROR: ").d(PLEASE_CHECK_INTERNET_CONNECTION)
                    AppState.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }

                Timber.tag("TASKS: ").d("${fetchingHealthTimeout?.toList()}")
                AppState.Success(fetchingHealthTimeout?.toList() ?: emptyList())
            }
        } catch (exception: Exception) {
            Timber.tag("ERROR: ").d(exception)
            AppState.Failure(exception = exception)
        }
    }

    override suspend fun deleteHealth(healthId: String): AppState<Unit> {
        return try {
            withContext(ioDispatcher) {
                val addTaskTimeout = withTimeoutOrNull(10000L) {
                    appLesson6Db.collection(COLLECTION_PATH_NAME)
                        .document(healthId)
                        .delete()
                }

                if (addTaskTimeout == null) {
                    Timber.tag("ERROR: ").d(PLEASE_CHECK_INTERNET_CONNECTION)
                    AppState.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }
                AppState.Success(Unit)
            }
        } catch (exception: Exception) {
            Timber.tag("ERROR: ").d(exception)
            AppState.Failure(exception = exception)
        }
    }

    override suspend fun updateHealth(
        healthId: String,
        upperPressure: String,
        lowerPressure: String,
        pulse: String
    ): AppState<Unit> {
        return try {
            withContext(ioDispatcher) {
                val healthUpdate: Map<String, Any> = hashMapOf(
                    "upperPressure" to upperPressure,
                    "lowerPressure" to lowerPressure,
                    "pulse" to pulse
                )

                val addTaskTimeout = withTimeoutOrNull(10000L) {
                    appLesson6Db.collection(COLLECTION_PATH_NAME)
                        .document(healthId)
                        .update(healthUpdate)
                }

                if (addTaskTimeout == null) {
                    Timber.tag("ERROR: ").d(PLEASE_CHECK_INTERNET_CONNECTION)
                    AppState.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }
                AppState.Success(Unit)
            }
        } catch (exception: Exception) {
            Timber.tag("ERROR: ").d(exception)
            AppState.Failure(exception = exception)
        }
    }
}
