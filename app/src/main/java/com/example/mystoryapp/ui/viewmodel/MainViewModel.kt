package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.repository.StoryRepository
import com.example.mystoryapp.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val usersRepository: UsersRepository, private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories(token: String) = storyRepository.getStories(token)
    fun getUserToken() = usersRepository.getUserToken().asLiveData(Dispatchers.IO)

    fun logout() {
        viewModelScope.launch {
            usersRepository.logout()
        }
    }
}