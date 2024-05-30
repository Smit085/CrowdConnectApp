package com.example.crowdconnectapp.di

import com.example.crowdconnectapp.models.AuthViewModel
import com.example.crowdconnectapp.models.QuizViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActivityModule {
    @Singleton
    @Provides
    fun provideQuizViewModel(): QuizViewModel {
        return QuizViewModel()
    }
    @Singleton
    @Provides
    fun provideAuthViewModel(): AuthViewModel {
        return AuthViewModel()
    }
}