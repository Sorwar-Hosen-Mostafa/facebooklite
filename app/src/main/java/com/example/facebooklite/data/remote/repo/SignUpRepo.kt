package com.example.facebooklite.data.remote.repo

import com.example.facebooklite.ResponseData
import com.example.facebooklite.data.remote.api.ApiService
import com.example.facebooklite.data.remote.header.ApiMainHeadersProvider
import com.example.facebooklite.model.User
import com.example.facebooklite.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SignUpRepo @Inject constructor(
    private val apiService: ApiService,
    private val apiMainHeadersProvider: ApiMainHeadersProvider
) {

    suspend fun signUp(
        partMap: Map<String, RequestBody>,
    ): Resource<ResponseData<User>> {

        val response = apiService.signUp(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            partMap
        )
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