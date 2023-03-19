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
class SignUpViewModel @Inject constructor(var _userRepo: UserRepo) : ViewModel() {

    private val _signUpResponseLiveData = MutableLiveData<Resource<ResponseData<User>>>()
    val signUpResponseLiveData: LiveData<Resource<ResponseData<User>>> get() = _signUpResponseLiveData

    fun signUp(
        email: String,
        name: String,
        password: String,
        phone: String,
        address: String,
        profilePic: File?
    ) {


        viewModelScope.launch {
            try {
                val resource = _userRepo.signUp(
                    getRequestBodyMapForSignUp(
                        email,
                        name,
                        password,
                        phone,
                        address,
                        profilePic
                    ), createImagePart(profilePic)
                )
                _signUpResponseLiveData.postValue(resource)
            } catch (e: Exception) {
                _signUpResponseLiveData.postValue(Resource.error(e.message!!, null))
            }

        }
    }

    private fun createImagePart(image: File?) =
        image?.let {
            val imageRequestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("picture", it.name, imageRequestBody)
        }


    fun getRequestBodyMapForSignUp(
        email: String,
        name: String,
        password: String,
        phone: String,
        address: String,
        profilePic: File?,
    ): HashMap<String, RequestBody> {
        val hashMap = HashMap<String, RequestBody>()

        /* val byteArrayOutputStream = ByteArrayOutputStream()

         profilePic?.let {
             profilePic.compress(
                 Bitmap.CompressFormat.JPEG,
                 80,
                 byteArrayOutputStream
             )
             val requestBody: RequestBody =
                 MultipartBody.Builder().setType(MultipartBody.FORM)
                     .addFormDataPart(
                         "picture",
                         "image_${Date().time}.jpg",
                         byteArrayOutputStream.toByteArray()
                             .toRequestBody("image/jpg".toMediaTypeOrNull())
                     )
                     .build()

             hashMap["picture"] = requestBody
         }*/

        hashMap["email"] = email.toRequestBody("text/plain".toMediaType())
        hashMap["name"] = name.toRequestBody("text/plain".toMediaType())
        hashMap["password"] = password.toRequestBody("text/plain".toMediaType())
        hashMap["phone"] = phone.toRequestBody("text/plain".toMediaType())
        hashMap["address"] = address.toRequestBody("text/plain".toMediaType())
        return hashMap
    }

}