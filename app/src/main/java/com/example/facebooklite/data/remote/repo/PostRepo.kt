package com.example.facebooklite.data.remote.repo

import com.example.facebooklite.ResponseData
import com.example.facebooklite.data.remote.api.ApiService
import com.example.facebooklite.data.remote.header.ApiMainHeadersProvider
import com.example.facebooklite.model.Comment
import com.example.facebooklite.model.Like
import com.example.facebooklite.model.User
import com.example.facebooklite.utils.Resource
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PostRepo @Inject constructor(
    private val apiService: ApiService,
    private val apiMainHeadersProvider: ApiMainHeadersProvider
) {

    suspend fun getCommentsByPost(
       postId: Long
    ): Resource<ResponseData<ArrayList<Comment>>> {

        val response = apiService.getAllComments(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            postId
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

    suspend fun getLikesByPost(
        postId: Long
    ): Resource<ResponseData<ArrayList<Like>>> {

        val response = apiService.getAllLikes(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            postId
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

    suspend fun commentOnPost(
        comment: Comment
    ): Resource<ResponseData<Comment>> {

        val response = apiService.comment(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            Gson().toJson(comment).toRequestBody("text/plain".toMediaType())
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