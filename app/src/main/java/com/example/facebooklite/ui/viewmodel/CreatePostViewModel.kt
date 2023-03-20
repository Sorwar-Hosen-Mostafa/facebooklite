package com.example.facebooklite.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebooklite.utils.ResponseData
import com.example.facebooklite.data.remote.repo.PostRepo
import com.example.facebooklite.model.Post
import com.example.facebooklite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
class CreatePostViewModel @Inject constructor(var _postRepo : PostRepo) : ViewModel() {

    private val _postLiveData = MutableLiveData<Resource<ResponseData<Post>>>()
    val postLiveData: LiveData<Resource<ResponseData<Post>>> get() = _postLiveData

    fun addPost(
        title: String,
        content: String,
        postImage: File?
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resource = _postRepo.addPost(
                    getRequestBodyMapForSignUp(
                        title,
                        content,
                    ), createImagePart(postImage)
                )
                _postLiveData.postValue(resource)
            } catch (e: Exception) {
                _postLiveData.postValue(Resource.error(e.message!!, null))
            }

        }
    }

    private fun createImagePart(image: File?) =
        image?.let {
            val imageRequestBody = image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("post_image", image.name, imageRequestBody)
        }



    private fun getRequestBodyMapForSignUp(
        title: String,
        content: String,
    ): HashMap<String, RequestBody> {
        val hashMap = HashMap<String, RequestBody>()
        hashMap["title"] = title.toRequestBody("text/plain".toMediaType())
        hashMap["content"] = content.toRequestBody("text/plain".toMediaType())
        return hashMap
    }


}