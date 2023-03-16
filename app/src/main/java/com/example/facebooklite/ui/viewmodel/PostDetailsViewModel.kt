package com.example.facebooklite.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebooklite.ResponseData
import com.example.facebooklite.data.remote.repo.PostRepo
import com.example.facebooklite.model.Comment
import com.example.facebooklite.model.Like
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
import kotlin.collections.ArrayList


@HiltViewModel
class PostDetailsViewModel @Inject constructor(var _postRepo: PostRepo) : ViewModel() {

    private val _commentsLiveData = MutableLiveData<Resource<ResponseData<ArrayList<Comment>>>>()
    val commentsLiveData: LiveData<Resource<ResponseData<ArrayList<Comment>>>> get() = _commentsLiveData

    private val _likesLiveData = MutableLiveData<Resource<ResponseData<ArrayList<Like>>>>()
    val likesLiveData: LiveData<Resource<ResponseData<ArrayList<Like>>>> get() = _likesLiveData

    private val _commentLiveData = MutableLiveData<Resource<ResponseData<Comment>>>()
    val commentLiveData: LiveData<Resource<ResponseData<Comment>>> get() = _commentLiveData

    private val _likeLiveData = MutableLiveData<Resource<ResponseData<Like>>>()
    val likeLiveData: LiveData<Resource<ResponseData<Like>>> get() = _likeLiveData

    private val _unlikeLiveData = MutableLiveData<Resource<ResponseData<Like>>>()
    val unlikeLiveData: LiveData<Resource<ResponseData<Like>>> get() = _unlikeLiveData

    fun getAllComments(
        postId: Long
    ) {
        viewModelScope.launch {
            try {
                val resource = _postRepo.getCommentsByPost(
                  postId
                )
                _commentsLiveData.postValue(resource)
            } catch (e: Exception) {
                _commentsLiveData.postValue(Resource.error(e.message!!, null))
            }

        }
    }

    fun comment(
        comment: Comment,
    ) {
        viewModelScope.launch {
            try {
                val resource = _postRepo.commentOnPost(
                    comment
                )
                _commentLiveData.postValue(resource)
            } catch (e: Exception) {
                _commentLiveData.postValue(Resource.error(e.message!!, null))
            }

        }
    }

    fun likePost(postId: Long) {
        viewModelScope.launch {
            try {
                val resource = _postRepo.likePost(postId.toString())
                _likeLiveData.postValue(resource)
            } catch (e: Exception) {
                _likeLiveData.postValue(Resource.error(e.message!!, null))
            }
        }
    }

    fun unlikePost(postId: Long) {
        viewModelScope.launch {
            try {
                val resource = _postRepo.unlikePost(postId.toString())
                _unlikeLiveData.postValue(resource)
            } catch (e: Exception) {
                _unlikeLiveData.postValue(Resource.error(e.message!!, null))
            }
        }
    }

    fun getAllLikes(
        postId: Long
    ) {
        viewModelScope.launch {
            try {
                val resource = _postRepo.getLikesByPost(
                    postId
                )
                _likesLiveData.postValue(resource)
            } catch (e: Exception) {
                _likesLiveData.postValue(Resource.error(e.message!!, null))
            }

        }
    }

}