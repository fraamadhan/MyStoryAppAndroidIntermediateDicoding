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
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.repository.ResultState
import com.example.mystoryapp.ui.viewmodel.RegisterViewModel
import com.example.mystoryapp.ui.viewmodel.factory.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private var binding : ActivityRegisterBinding? = null
    private var name: String = ""
    private var email: String = ""
    private var password: String = ""

    private val registerMainViewModel by viewModels<RegisterViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnRegisterPage?.isEnabled = false
        setupAction()

        binding?.navtologin?.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding?.btnRegisterPage?.setOnClickListener {
            loading()
            binding?.apply {
                name = nameEditText.text.toString().trim()
                email = emailEditText.text.toString().trim()
                password = passwordEditText.text.toString().trim()
            }
           registerMainViewModel.addNewUser(name, email, password).observe(this@RegisterActivity) { result ->
               when(result) {
                   is ResultState.Loading -> {
                       loading()
                   }
                   is ResultState.Success -> {
                       success()
                   }
                   is ResultState.Error -> {
                       errorToast(getString(R.string.unique_email))
                   }
               }
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
            val nameTv = ObjectAnimator.ofFloat(nameTextView, View.ALPHA, 1f).setDuration(1000)
            val nameEdt =
                ObjectAnimator.ofFloat(nameEditTextLayout, View.ALPHA, 1f).setDuration(1000)
            val emailTv = ObjectAnimator.ofFloat(emailTextView, View.ALPHA, 1f).setDuration(1000)
            val emailEdt =
                ObjectAnimator.ofFloat(emailEditTextLayout, View.ALPHA, 1f).setDuration(1000)
            val passTv = ObjectAnimator.ofFloat(passwordTextView, View.ALPHA, 1f).setDuration(1000)
            val passEdt =
                ObjectAnimator.ofFloat(passwordEditTextLayout, View.ALPHA, 1f).setDuration(1000)
            val loginBtn = ObjectAnimator.ofFloat(btnRegisterPage, View.ALPHA, 1f).setDuration(1000)
            val navTologin = ObjectAnimator.ofFloat(navtologin, View.ALPHA, 1f).setDuration(1000)

            val playName = AnimatorSet().apply {
                playTogether(nameTv, nameEdt)
            }
            val playEmail = AnimatorSet().apply {
                playTogether(emailTv, emailEdt)
            }
            val playPassword = AnimatorSet().apply {
                playTogether(passTv, passEdt)
            }
            AnimatorSet().apply {
                playTogether(boo1, boo2)

                playSequentially(title, playName, playEmail, playPassword, loginBtn, navTologin)
                startDelay = 100
            }.start()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun setupAction() {
        binding?.apply {
            emailEditText.addTextChangedListener(watcher)
            passwordEditText.addTextChangedListener(watcher)
            nameEditText.addTextChangedListener(watcher)
        }
    }

    private fun loading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun success() {
        binding?.progressBar?.visibility = View.GONE
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun errorToast(message: String) {
        binding?.progressBar?.visibility = View.GONE
        showToast(message)
    }

    private fun setMyButtonEnabled() {
        val email = binding?.emailEditText
        val password = binding?.passwordEditText
        val name = binding?.nameEditText?.text

        if (email?.text!!.isNotEmpty() && password?.text!!.isNotEmpty() && name!!.isNotEmpty()) {
            binding?.btnRegisterPage?.isEnabled = email?.error == null && password?.error == null && name!!.isNotEmpty()
        }
        else {
            binding?.btnRegisterPage?.isEnabled = false
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