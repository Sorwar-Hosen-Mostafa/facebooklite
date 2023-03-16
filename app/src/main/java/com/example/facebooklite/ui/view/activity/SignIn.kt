package com.example.facebooklite.ui.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.facebooklite.R
import com.example.facebooklite.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        viewClickEvents()

    }

    private fun viewClickEvents() {
        binding.signUpButton.setOnClickListener {
           Intent(this, SignUp::class.java).run {
               startActivity(this)
           }
        }

        binding.signInButton.setOnClickListener {
            Intent(this, MainActivity::class.java).run {
                startActivity(this)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}