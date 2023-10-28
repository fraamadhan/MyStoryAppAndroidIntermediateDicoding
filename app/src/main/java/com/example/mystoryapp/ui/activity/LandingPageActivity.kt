package com.example.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.databinding.ActivityLandingPageBinding
import com.example.mystoryapp.ui.activity.auth.LoginActivity
import com.example.mystoryapp.ui.activity.auth.RegisterActivity

class LandingPageActivity : AppCompatActivity() {

    private var binding : ActivityLandingPageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnLogin?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding?.btnRegister?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}