package com.example.mystoryapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mystoryapp.data.api.ApiService
import com.example.mystoryapp.data.response.GeneralResponse
import com.example.mystoryapp.data.response.LoginResponse
import com.example.mystoryapp.pref.LoginPreferences
import com.example.mystoryapp.pref.UserModel
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UsersRepository private constructor(
    private val apiService: ApiService,
    private val dataStore: LoginPreferences,
) {

    fun addNewUser(name: String, email: String, password: String) : LiveData<ResultState<GeneralResponse>> = liveData {
        emit(ResultState.Loading)
        try{
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            emit(ResultState.Error(e.toString()))
        }
    }

    fun userLogin(email: String, password: String) : LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try{
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch(e : HttpException) {
            emit(ResultState.Error(e.toString()))
        }
    }

    suspend fun saveSession(userModel: UserModel) {
        dataStore.saveSession(userModel)
    }

    fun getSession() : Flow<UserModel> {
        return dataStore.getSession()
    }

    fun getUserToken() = dataStore.getUserToken()

    suspend fun logout() {
        dataStore.logout()
    }

    companion object {
        @Volatile
        private var instance: UsersRepository? = null

        fun getInstance(
            apiService: ApiService,
            loginPreferences: LoginPreferences
        ) : UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService, loginPreferences)
            }.also { instance = it }
    }
}
