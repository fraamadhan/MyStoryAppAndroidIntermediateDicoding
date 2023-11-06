package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapp.repository.StoryRepository
import com.example.mystoryapp.repository.UsersRepository
import kotlinx.coroutines.Dispatchers

class MapsViewModel(private val usersRepository: UsersRepository, private val storyRepository: StoryRepository): ViewModel() {
    fun getStoriesWithLocation(token: String) = storyRepository.getStoriesWithLocation(token)
    fun getUserToken() = usersRepository.getUserToken().asLiveData(Dispatchers.IO)
}