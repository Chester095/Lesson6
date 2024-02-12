package com.example.lesson6.di

import com.example.lesson6.data.repositories.HealthRepository
import com.example.lesson6.data.repositories.HealthRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        firebaseFirestore: FirebaseFirestore,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ):HealthRepository{
        return HealthRepositoryImpl(
            appLesson6Db = firebaseFirestore,
            ioDispatcher = ioDispatcher

        )
    }
}