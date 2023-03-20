package com.example.facebooklite.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebooklite.utils.ResponseData
import com.example.facebooklite.data.remote.repo.UserRepo
import com.example.facebooklite.model.User
import com.example.facebooklite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(var _userRepo: UserRepo) : ViewModel() {

    private val _signInResponseLiveData = MutableLiveData<Resource<ResponseData<User>>>()
    val signInResponseLiveData: LiveData<Resource<ResponseData<User>>> get() = _signInResponseLiveData

    fun signIn(
        email: String,
        password: String,
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resource = _userRepo.signIn(
                    email,
                    password,
                )
                _signInResponseLiveData.postValue(resource)
            } catch (e: Exception) {
                _signInResponseLiveData.postValue(Resource.error(e.message!!, null))
            }

        }
    }
}