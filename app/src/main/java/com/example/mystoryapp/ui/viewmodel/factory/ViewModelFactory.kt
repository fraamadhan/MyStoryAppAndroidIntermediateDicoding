package com.example.mystoryapp.ui.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.data.di.Injection
import com.example.mystoryapp.repository.StoryRepository
import com.example.mystoryapp.repository.UsersRepository
import com.example.mystoryapp.ui.viewmodel.DetailViewModel
import com.example.mystoryapp.ui.viewmodel.LandingPageViewModel
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import com.example.mystoryapp.ui.viewmodel.RegisterViewModel
import com.example.mystoryapp.ui.viewmodel.UploadViewModel

class ViewModelFactory private constructor(
    private val usersRepository: UsersRepository,
    private val storyRepository: StoryRepository,
) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(usersRepository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(usersRepository) as T
        }
        if (modelClass.isAssignableFrom(LandingPageViewModel::class.java)) {
            return LandingPageViewModel(usersRepository) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(usersRepository, storyRepository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(usersRepository, storyRepository) as T
        }
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(storyRepository, usersRepository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel CLass: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideStoryRepository(context),
                )
            }.also { instance = it }
    }

}