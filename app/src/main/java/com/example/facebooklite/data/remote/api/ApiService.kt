package com.example.facebooklite.data.remote.api

import com.example.facebooklite.MainApiHeaders
import com.example.facebooklite.ResponseData
import com.example.facebooklite.model.Comment
import com.example.facebooklite.model.Like
import com.example.facebooklite.model.Post
import com.example.facebooklite.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Multipart
    @JvmSuppressWildcards
    @POST(value = "signUp")
    suspend fun signUp(
        @HeaderMap mainApiHeaders: MainApiHeaders,
        @PartMap partMap: Map<String, RequestBody>,
        @Part image: MultipartBody.Part
    ): Response<ResponseData<User>>

    @Multipart
    @POST(value = "fileUpload")
    suspend fun fileUpload(
        @HeaderMap mainApiHeaders: MainApiHeaders,
        @Part image: MultipartBody.Part
    ): Response<ResponseData<User>>

    @POST(value = "likePost/{postId}}")
    suspend fun likePost(
        @HeaderMap mainApiHeaders: MainApiHeaders,
        @Path(value = "postId") postId: Long
    ): Response<ResponseData<Like>>

    @Headers("Content-Type: application/json")
    @POST(value = "comment")
    suspend fun comment(
        @HeaderMap mainApiHeaders: MainApiHeaders,
        @Body comment: RequestBody
    ): Response<ResponseData<Comment>>

    @POST(value = "signIn")
    suspend fun signIn(
        @HeaderMap mainApiHeaders: MainApiHeaders,
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<ResponseData<User>>

    @GET(value = "getAllPost")
    suspend fun getAllPost(
        @HeaderMap mainApiHeaders: MainApiHeaders
    ): Response<ResponseData<ArrayList<Post>>>


    @GET(value = "getPostById/{postId}")
    suspend fun getPostById(
        @HeaderMap mainApiHeaders: MainApiHeaders,
        @Path(value = "postId") postId: Long
    ): Response<ResponseData<Post>>

    @GET(value = "getAllLikes/{postId}")
    suspend fun getAllLikes(
        @HeaderMap mainApiHeaders: MainApiHeaders,
        @Path(value = "postId") postId: Long
    ): Response<ResponseData<ArrayList<Like>>>

    @GET(value = "getAllComments/{postId}")
    suspend fun getAllComments(
        @HeaderMap mainApiHeaders: MainApiHeaders,
        @Path(value = "postId") postId: Long
    ): Response<ResponseData<ArrayList<Comment>>>

}