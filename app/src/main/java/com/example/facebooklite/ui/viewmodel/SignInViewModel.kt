package com.example.facebooklite.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebooklite.ResponseData
import com.example.facebooklite.data.remote.repo.UserRepo
import com.example.facebooklite.model.User
import com.example.facebooklite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(var _userRepo: UserRepo) : ViewModel() {

    private val _signInResponseLiveData = MutableLiveData<Resource<ResponseData<User>>>()
    val signInResponseLiveData: LiveData<Resource<ResponseData<User>>> get() = _signInResponseLiveData

    fun signIn(
        email: String,
        password: String,
    ) {

        viewModelScope.launch {
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