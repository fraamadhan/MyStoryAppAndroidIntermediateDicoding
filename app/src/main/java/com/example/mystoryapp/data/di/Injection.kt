package com.example.mystoryapp.data.di

import android.content.Context
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.pref.LoginPreferences
import com.example.mystoryapp.pref.dataStore
import com.example.mystoryapp.repository.StoryRepository
import com.example.mystoryapp.repository.UsersRepository

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UsersRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService)
    }
}