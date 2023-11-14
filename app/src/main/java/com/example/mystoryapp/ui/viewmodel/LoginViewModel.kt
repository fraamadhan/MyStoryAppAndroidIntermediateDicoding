package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.response.LoginResponse
import com.example.mystoryapp.pref.UserModel
import com.example.mystoryapp.repository.ResultState
import com.example.mystoryapp.repository.UsersRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val usersRepository: UsersRepository) : ViewModel() {
    private val loginResult: LiveData<ResultState<LoginResponse>> = MutableLiveData()

    // Fungsi untuk mendapatkan LiveData hasil login
    fun getUserLogin(): LiveData<ResultState<LoginResponse>> {
        return loginResult
    }

    fun userLogin(email: String, password: String) {
        viewModelScope.launch {
            val loginResultLiveData = usersRepository.userLogin(email, password)

            loginResultLiveData.observeForever { result ->
                (loginResult as MutableLiveData).value = result
            }
        }
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            usersRepository.saveSession(user)
        }
    }
}