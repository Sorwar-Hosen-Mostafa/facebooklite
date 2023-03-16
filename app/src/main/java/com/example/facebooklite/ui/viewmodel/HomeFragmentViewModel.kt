package com.example.facebooklite.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebooklite.ResponseData
import com.example.facebooklite.data.remote.repo.HomeRepo
import com.example.facebooklite.model.Post
import com.example.facebooklite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeFragmentViewModel @Inject constructor(var _homeRepo: HomeRepo) : ViewModel() {

    private val _allPostLiveData = MutableLiveData<Resource<ResponseData<ArrayList<Post>>>>()
    val allPostLiveData: LiveData<Resource<ResponseData<ArrayList<Post>>>> get() = _allPostLiveData

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

}