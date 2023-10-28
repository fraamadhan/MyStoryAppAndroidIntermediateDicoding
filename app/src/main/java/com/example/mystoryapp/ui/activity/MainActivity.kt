package com.example.mystoryapp.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.StoryAdapter
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.repository.ResultState
import com.example.mystoryapp.ui.activity.auth.LoginActivity
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import com.example.mystoryapp.ui.viewmodel.factory.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)
        adapter = StoryAdapter()
        binding.rvStories.adapter = adapter

        mainViewModel.getUserToken().observe(this@MainActivity) {userToken ->
            mainViewModel.getStories(userToken.toString()).observe(this@MainActivity) {result ->
                when(result){
                    is ResultState.Success -> {
                        success(result.data)
                    }
                    is ResultState.Error -> {
                        errorToast(getString(R.string.logout_status))
                    }
                    is ResultState.Loading -> {
                        loading()
                    }
                }
            }
        }
        addNewStory()
        logout()

    }

    private fun loading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun success(stories: List<ListStoryItem>) {
        binding?.progressBar?.visibility = View.GONE
        adapter.submitList(stories)

    }

    private fun addNewStory() {
        binding?.fabAddStory?.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadStoryActivity::class.java))
        }
    }

    private fun logout() {
        binding.mainToolbar.setNavigationOnClickListener {view ->
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.logout_alert))
                .setMessage(getString(R.string.logout_message))
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                    mainViewModel.logout()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                })
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun errorToast(errorMessage: String) {
        binding?.progressBar?.visibility = View.GONE
        showToast(errorMessage)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}