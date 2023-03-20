package com.example.facebooklite.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebooklite.utils.ResponseData
import com.example.facebooklite.data.remote.repo.PostRepo
import com.example.facebooklite.model.Like
import com.example.facebooklite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class LikerFragmentViewModel @Inject constructor(var _postRepo : PostRepo) : ViewModel() {

    private val _likerLiveData = MutableLiveData<Resource<ResponseData<ArrayList<Like>>>>()
    val likerLiveData: LiveData<Resource<ResponseData<ArrayList<Like>>>> get() = _likerLiveData

    fun getAllLikes(
        postId: Long
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resource = _postRepo.getLikesByPost(
                    postId
                )
                _likerLiveData.postValue(resource)
            } catch (e: Exception) {
                _likerLiveData.postValue(Resource.error(e.message!!, null))
            }

        }
    }
}