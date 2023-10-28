package com.example.mystoryapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mystoryapp.data.api.ApiService
import com.example.mystoryapp.data.response.GeneralResponse
import com.example.mystoryapp.data.response.ListStoryItem
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
) {

    fun getStories(token: String) : LiveData<ResultState<List<ListStoryItem>>> = liveData {
        emit(ResultState.Loading)
        try{
            val successResponse = apiService.getStories("Bearer $token")
            emit(ResultState.Success(successResponse.listStory))
        } catch(e: HttpException) {
            emit(ResultState.Error(e.toString()))
        }
    }

    fun getDetailStory(token: String, storyId: String) : LiveData<ResultState<ListStoryItem>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getDetailStory("Bearer $token", storyId)
            emit(ResultState.Success(successResponse.story))
        } catch(e: HttpException) {
            emit(ResultState.Error(e.toString()))
        }
    }

    fun uploadImage(token:String, description: String, imageFile: File) = liveData<ResultState<GeneralResponse>> {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile =imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadStory("Bearer $token", requestBody, multipartBody)
            emit(ResultState.Success(successResponse))
        } catch(e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GeneralResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
        ) : StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}
