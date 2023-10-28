package com.example.mystoryapp.ui.activity.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.pref.UserModel
import com.example.mystoryapp.repository.ResultState
import com.example.mystoryapp.ui.activity.MainActivity
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import com.example.mystoryapp.ui.viewmodel.factory.ViewModelFactory
import com.example.mystoryapp.utils.isOnline

class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null
    private val loginViewModel by viewModels<LoginViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnLoginPage?.isEnabled = false
        setupAction()

        binding?.navtoregister?.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding?.btnLoginPage?.setOnClickListener {
            loading()
            val email = binding?.emailEditText?.text.toString().trim()
            val password = binding?.passwordEditText?.text.toString().trim()

            if(isOnline(this)) {
                loginViewModel.userLogin(email, password).observe(this@LoginActivity) {result ->
                    when(result) {
                        is ResultState.Success -> {
                            success()
                            val userId = result.data.loginResult?.userId
                            val name = result.data.loginResult?.name
                            val token = result.data.loginResult?.token
                            val user = UserModel(userId!!, name!!, token!!, true)
                            loginViewModel.saveSession(user)
                        }
                        is ResultState.Error -> {
                            errorToast(result.error)
                        }
                        is ResultState.Loading -> {
                            loading()
                        }
                    }
                }
            } else {
                errorToast(getString(R.string.check_connection_message))

            }

        }
        playAnimation()

    }

    private fun playAnimation() {
        val boo1 = ObjectAnimator.ofFloat(binding?.boo1, View.TRANSLATION_X, 100F, -70F).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val boo2 = ObjectAnimator.ofFloat(binding?.boo2, View.TRANSLATION_X, -70F, 70F).apply {
            duration = 5700
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        binding?.apply {
            val title = ObjectAnimator.ofFloat(titleTextView, View.ALPHA, 1f).setDuration(1000)
            val msg = ObjectAnimator.ofFloat(messageTextView, View.ALPHA, 1f).setDuration(1000)
            val emailTv = ObjectAnimator.ofFloat(emailTextView, View.ALPHA, 1f).setDuration(1000)
            val emailEdt =
                ObjectAnimator.ofFloat(emailEditTextLayout, View.ALPHA, 1f).setDuration(1000)
            val passTv = ObjectAnimator.ofFloat(passwordTextView, View.ALPHA, 1f).setDuration(1000)
            val passEdt =
                ObjectAnimator.ofFloat(passwordEditTextLayout, View.ALPHA, 1f).setDuration(1000)
            val loginBtn = ObjectAnimator.ofFloat(btnLoginPage, View.ALPHA, 1f).setDuration(1000)
            val navToReg = ObjectAnimator.ofFloat(navtoregister, View.ALPHA, 1f).setDuration(1000)

            AnimatorSet().apply {
                playTogether(boo1, boo2)
                playSequentially(title, msg, emailTv, emailEdt, passTv, passEdt, loginBtn, navToReg)
                startDelay = 100
            }.start()
        }
    }

    private fun setupAction() {
        binding?.apply {
            emailEditText.addTextChangedListener(watcher)
            passwordEditText.addTextChangedListener(watcher)
        }
    }

    private fun loading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun success() {
        binding?.progressBar?.visibility = View.GONE
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun errorToast(errorMessage: String) {
        binding?.progressBar?.visibility = View.GONE
        showToast(errorMessage)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setMyButtonEnabled() {
        val email = binding?.emailEditText
        val password = binding?.passwordEditText

        if (email?.text!!.isNotEmpty() && password?.text!!.isNotEmpty()) {
            binding?.btnLoginPage?.isEnabled = email?.error == null && password?.error == null
        }
        else {
            binding?.btnLoginPage?.isEnabled = false
        }
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
           //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setMyButtonEnabled()
        }

        override fun afterTextChanged(s: Editable?) {
            //
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}