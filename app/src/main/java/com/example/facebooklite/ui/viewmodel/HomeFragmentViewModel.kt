package com.example.facebooklite.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebooklite.ResponseData
import com.example.facebooklite.data.remote.repo.HomeRepo
import com.example.facebooklite.data.remote.repo.PostRepo
import com.example.facebooklite.model.Like
import com.example.facebooklite.model.Post
import com.example.facebooklite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeFragmentViewModel @Inject constructor(var _homeRepo: HomeRepo, var _postRepo : PostRepo) : ViewModel() {

    private val _allPostLiveData = MutableLiveData<Resource<ResponseData<ArrayList<Post>>>>()
    val allPostLiveData: LiveData<Resource<ResponseData<ArrayList<Post>>>> get() = _allPostLiveData

    private val _likeLiveData = MutableLiveData<Resource<ResponseData<Like>>>()
    val likeLiveData: LiveData<Resource<ResponseData<Like>>> get() = _likeLiveData

    private val _unlikeLiveData = MutableLiveData<Resource<ResponseData<Like>>>()
    val unlikeLiveData: LiveData<Resource<ResponseData<Like>>> get() = _unlikeLiveData

    fun getAllPost() {
        viewModelScope.launch {
            try {
                val resource = _homeRepo.getAllPost()
                _allPostLiveData.postValue(resource)
            } catch (e: Exception) {
                _allPostLiveData.postValue(Resource.error(e.message!!, null))
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

}