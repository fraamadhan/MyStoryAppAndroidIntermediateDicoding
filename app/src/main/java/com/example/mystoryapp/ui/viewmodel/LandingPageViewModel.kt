package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapp.pref.UserModel
import com.example.mystoryapp.repository.UsersRepository

class LandingPageViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    fun getSession() : LiveData<UserModel> {
        return usersRepository.getSession().asLiveData()
    }
}