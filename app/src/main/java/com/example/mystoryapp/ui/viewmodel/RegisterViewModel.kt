package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.repository.UsersRepository

class RegisterViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    fun addNewUser(name: String, email: String, password: String) = usersRepository.addNewUser(name, email, password)

}
