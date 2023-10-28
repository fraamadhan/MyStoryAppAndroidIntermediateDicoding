package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.pref.UserModel
import com.example.mystoryapp.repository.UsersRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val usersRepository: UsersRepository) : ViewModel() {
    fun userLogin(email: String, password: String) = usersRepository.userLogin(email, password)
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            usersRepository.saveSession(user)
        }
    }
}