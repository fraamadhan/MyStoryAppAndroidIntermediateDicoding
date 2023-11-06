package com.example.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.LoadingStateAdapter
import com.example.mystoryapp.adapter.StoryAdapter
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.activity.auth.LoginActivity
import com.example.mystoryapp.ui.maps.MapsActivity
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
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.title = ""

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)
        adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        loading()
        mainViewModel.getUserToken().observe(this@MainActivity) {userToken ->
            mainViewModel.getStories(userToken.toString()).observe(this@MainActivity) {result ->
                success(result)
            }
        }
        addNewStory()
    }

    private fun loading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            rvStories.visibility = View.GONE
        }

    }

    private fun success(stories: PagingData<ListStoryItem>) {
        binding.apply {
            progressBar.visibility = View.GONE
            rvStories.visibility = View.VISIBLE
        }
        adapter.submitData(lifecycle, stories)

    }

    private fun addNewStory() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            R.id.map_menu -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun logout() {

        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getString(R.string.logout_alert))
            .setMessage(getString(R.string.logout_message))
            .setPositiveButton("Yes"){ _, _ ->
                mainViewModel.logout()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

}