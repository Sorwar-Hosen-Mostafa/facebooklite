package com.example.facebooklite.data.remote.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.facebooklite.utils.ResponseData
import com.example.facebooklite.data.remote.api.ApiService
import com.example.facebooklite.data.remote.header.ApiMainHeadersProvider
import com.example.facebooklite.model.Comment
import com.example.facebooklite.model.Like
import com.example.facebooklite.model.Post
import com.example.facebooklite.model.User
import com.example.facebooklite.utils.Resource
import com.google.gson.Gson
import com.google.gson.JsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class PostRepo @Inject constructor(
    private val apiService: ApiService,
    private val apiMainHeadersProvider: ApiMainHeadersProvider,
    private val gson: Gson
) {

    suspend fun addPost(
        partMap: Map<String, RequestBody>,
        image: MultipartBody.Part?
    ): Resource<ResponseData<Post>> {

        val response = apiService.addPost(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            partMap, image
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

    suspend fun getAllPost(userId: Long?): Resource<ResponseData<ArrayList<Post>>> {
        val response = apiService.getAllPost(
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

    suspend fun getCommentsByPost(
        postId: Long
    ): Resource<ResponseData<ArrayList<Comment>>> {

        val response = apiService.getAllComments(
            apiMainHeadersProvider.getAuthenticatedHeaders(),
            postId.toString()
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
            postId.toString()
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
            gson.toJson(comment).toRequestBody("text/plain".toMediaType())
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


    suspend fun likePost(
        postId: String
    ): Resource<ResponseData<Like>> {

        val response = apiService.likePost(
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

    suspend fun unlikePost(
        postId: String
    ): Resource<ResponseData<Like>> {

        val response = apiService.unlikePost(
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


}