package com.example.mystoryapp.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapp.repository.StoryRepository
import com.example.mystoryapp.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import java.io.File

class UploadViewModel(private val storyRepository: StoryRepository, private val usersRepository: UsersRepository) : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun getUserToken() = usersRepository.getUserToken().asLiveData(Dispatchers.IO)
    fun uploadStory(token: String, description: String, imageFile: File) = storyRepository.uploadImage(token, description, imageFile)
}