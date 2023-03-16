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

    suspend fun getAllPost(): Resource<ResponseData<ArrayList<Post>>> {

        val response = apiService.getAllPost(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
        )

        /*val response = apiService.fileUpload(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            image
        )*/

        return when (response.code()) {
            200 -> {
                Resource.success("success", response.body())
            }
            403 -> {
                response.errorBody()?.let { it ->
                    Resource.error(it.string(), null)
                } ?: Resource.error("Something went wrong", null)
            }
            else -> {
                Resource.error("Something went wrong", null)
            }
        }
    }

}