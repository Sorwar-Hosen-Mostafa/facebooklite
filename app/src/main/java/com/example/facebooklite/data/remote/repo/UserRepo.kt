package com.example.facebooklite.data.remote.repo

import com.example.facebooklite.utils.ResponseData
import com.example.facebooklite.data.remote.api.ApiService
import com.example.facebooklite.data.remote.header.ApiMainHeadersProvider
import com.example.facebooklite.model.User
import com.example.facebooklite.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val apiService: ApiService,
    private val apiMainHeadersProvider: ApiMainHeadersProvider
) {

    suspend fun signUp(
        partMap: Map<String, RequestBody>,
        image: MultipartBody.Part?
    ): Resource<ResponseData<User>> {

        val response = apiService.signUp(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            partMap,image
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

    suspend fun signIn(
        email:String,
        password:String
    ): Resource<ResponseData<User>> {

        val response = apiService.signIn(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            email,password
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

    suspend fun uploadProfilePicture(image: MultipartBody.Part): Resource<ResponseData<User>>{
        val response = apiService.uploadProfilePicture(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            image
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

    suspend fun getUserData(
        userId:Long
    ): Resource<ResponseData<User>> {

        val response = apiService.getUserData(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            userId
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