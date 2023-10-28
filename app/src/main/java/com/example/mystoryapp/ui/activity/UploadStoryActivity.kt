package com.example.mystoryapp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityUploadStoryBinding
import com.example.mystoryapp.repository.ResultState
import com.example.mystoryapp.ui.viewmodel.UploadViewModel
import com.example.mystoryapp.ui.viewmodel.factory.ViewModelFactory
import com.example.mystoryapp.utils.getImageUri
import com.example.mystoryapp.utils.reduceImageFile
import com.example.mystoryapp.utils.uriToFile

class UploadStoryActivity : AppCompatActivity() {

    private var binding : ActivityUploadStoryBinding? = null
    private var currentImageUri: Uri? = null
    private val uploadViewModel by viewModels<UploadViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        uploadViewModel.imageUri.observe(this@UploadStoryActivity) {uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            }
        }

        uploadViewModel.imageUri.observe(this@UploadStoryActivity) {uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            }
        }

        binding?.apply {
            btnFromGalery.setOnClickListener {
                startGallery()
            }
            btnFromCamera.setOnClickListener {
                startCamera()
            }
            btnUpload.setOnClickListener {
                uploadImage()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {uri: Uri? ->
        if(uri != null) {
            currentImageUri = uri
            uploadViewModel.setImageUri(uri)
        } else {
            showToast(getString(R.string.media_not_selected))
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        uploadViewModel.setImageUri(currentImageUri)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if(isSuccess) showImage()
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceImageFile()
            val description = binding?.descriptionEditText?.text!!.toString().trim()
            showLoading(true)

            uploadViewModel.getUserToken().observe(this@UploadStoryActivity) {userToken ->
                uploadViewModel.uploadStory(userToken.toString(), description, imageFile).observe(this@UploadStoryActivity) {result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Success -> {
                                showLoading(false)
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            is ResultState.Error -> {
                                showLoading(false)
                                showToast(result.error)
                            }
                            is ResultState.Loading -> {
                                showLoading(true)
                            }
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))

    }

    private fun showImage() {
        currentImageUri?.let {
            binding?.previewImageView?.setImageURI(currentImageUri)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}