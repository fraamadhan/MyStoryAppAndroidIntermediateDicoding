package com.example.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mystoryapp.R
import com.example.mystoryapp.ui.viewmodel.LandingPageViewModel
import com.example.mystoryapp.ui.viewmodel.factory.ViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    private val landingPageViewModel by viewModels<LandingPageViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash_screen)

        landingPageViewModel.getSession().observe(this@SplashScreenActivity) {user ->
            if(!user.isLogin) {
                startActivity(Intent(this@SplashScreenActivity, LandingPageActivity::class.java))
                finish()
            }
            else{
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}