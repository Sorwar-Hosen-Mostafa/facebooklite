package com.example.facebooklite.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.facebooklite.R
import com.example.facebooklite.databinding.ActivitySignInBinding
import com.example.facebooklite.ui.view.base.BaseActivity
import com.example.facebooklite.ui.viewmodel.SignInViewModel
import com.example.facebooklite.utils.SharedPreferenceConfiguration
import com.example.facebooklite.utils.SharedPreferenceConfiguration.Companion.KEY_IS_LOGGED_IN
import com.example.facebooklite.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignIn : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val _signInViewModel: SignInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        if(SharedPreferenceConfiguration.getInstance(this).getBoolean(KEY_IS_LOGGED_IN)){
            Intent(this, MainActivity::class.java).run {
                startActivity(this)
            }
            finishAffinity()
        }else{
            prepareObservers()
            viewClickEvents()
        }


    }

    private fun viewClickEvents() {
        binding.signUpButton.setOnClickListener {
            Intent(this, SignUp::class.java).run {
                startActivity(this)
            }
        }

        binding.signInButton.setOnClickListener {
            if (isAllDataValid()) {
                binding.run {
                    _signInViewModel.signIn(
                        email = emailField.text.toString(),
                        password = passwordField.text.toString(),
                    )
                }

            }
        }
    }

    private fun prepareObservers() {
        _signInViewModel.signInResponseLiveData.observe(this) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    SharedPreferenceConfiguration.getInstance(this)
                        .setUserInfo(resource.data!!.data)
                    SharedPreferenceConfiguration.getInstance(this)
                        .putBoolean(KEY_IS_LOGGED_IN,true)
                    Intent(this, MainActivity::class.java).run {
                        startActivity(this)
                        finishAffinity()
                    }
                    binding.loaderLayout.root.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.loaderLayout.root.visibility = View.GONE
                    showSnackbar(resource.message.toString(),binding.root)
                }
                Status.LOADING -> {
                    binding.loaderLayout.root.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun isAllDataValid(): Boolean {
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}