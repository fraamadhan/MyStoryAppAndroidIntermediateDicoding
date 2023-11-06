package com.example.mystoryapp.data.di

import android.content.Context
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.database.StoryDatabase
import com.example.mystoryapp.pref.LoginPreferences
import com.example.mystoryapp.pref.dataStore
import com.example.mystoryapp.repository.StoryRepository
import com.example.mystoryapp.repository.UsersRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UsersRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val dataStore = LoginPreferences.getInstance(context.dataStore)
        val token = runBlocking { dataStore.getUserToken().first() }
        val apiService = ApiConfig.getApiService()
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(storyDatabase, apiService)
    }
}