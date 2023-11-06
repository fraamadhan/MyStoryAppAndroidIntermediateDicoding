package com.example.mystoryapp.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding
import com.example.mystoryapp.repository.ResultState
import com.example.mystoryapp.ui.viewmodel.DetailViewModel
import com.example.mystoryapp.ui.viewmodel.factory.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailStoryActivity : AppCompatActivity() {
    private var binding: ActivityDetailStoryBinding? = null

    private val detailViewModel by viewModels<DetailViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val storyId = intent.getStringExtra(STORY_ID)

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                detailViewModel.getUserToken().observe(this@DetailStoryActivity) {userToken ->
                    detailViewModel.getDetailStory(userToken.toString(), storyId.toString()).observe(this@DetailStoryActivity) {
                            result ->
                        when(result) {
                            is ResultState.Success -> {
                                success(result.data)
                            }
                            is ResultState.Error -> {
                                errorToast(result.error)
                            }
                            is ResultState.Loading -> {
                                loading()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun success(data: ListStoryItem) {
        binding?.apply {
            progressBar.visibility = View.GONE
            Glide.with(applicationContext)
                .load(data.photoUrl)
                .into(ivStory)
            tvNameStory.text = data.name
            tvDescriptionStory.text = data.description
        }
    }

    private fun errorToast(message: String) {
        binding?.progressBar?.visibility = View.GONE
        Toast.makeText(this@DetailStoryActivity, "Error: $message", Toast.LENGTH_SHORT).show()
    }
    companion object{
        const val STORY_ID = "story_id"
//        const val STORY_NAME = "story_name"
//        const val STORY_PICTURE = "story_picture"
//        const val STORY_DESCRIPTION = "story_description"
    }

}
