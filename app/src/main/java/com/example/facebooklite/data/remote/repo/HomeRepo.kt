package com.example.facebooklite.data.remote.repo

import com.example.facebooklite.utils.ResponseData
import com.example.facebooklite.data.remote.api.ApiService
import com.example.facebooklite.data.remote.header.ApiMainHeadersProvider
import com.example.facebooklite.model.Post
import com.example.facebooklite.utils.Resource
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val apiService: ApiService,
    private val apiMainHeadersProvider: ApiMainHeadersProvider
) {


}