package com.example.mystoryapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.data.StoryRemoteMediator
import com.example.mystoryapp.data.api.ApiService
import com.example.mystoryapp.data.response.GeneralResponse
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.database.StoryDatabase
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {

    fun getStories(token: String) : LiveData<PagingData<ListStoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 3,
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoriesWithLocation(token: String): LiveData<ResultState<List<ListStoryItem>>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStoriesWithLocation("Bearer $token")
            emit(ResultState.Success(successResponse.listStory))
        } catch (e: HttpException) {
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

    fun uploadImage(token: String, description: String, imageFile: File) = liveData<ResultState<GeneralResponse>> {
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
            storyDatabase: StoryDatabase,
            apiService: ApiService,
        ) : StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase, apiService)
            }.also { instance = it }
    }
}
