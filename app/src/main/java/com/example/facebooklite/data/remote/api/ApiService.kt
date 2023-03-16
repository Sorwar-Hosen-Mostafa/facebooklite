package com.example.facebooklite.data.remote.api

import com.example.facebooklite.MainApiHeaders
import com.example.facebooklite.ResponseData
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


    @GET(value = "getAllPost")
    suspend fun getAllPost(@HeaderMap mainApiHeaders: MainApiHeaders) : Response<ResponseData<ArrayList<Post>>>


    @POST(value = "signIn")
    suspend fun signIn(@HeaderMap mainApiHeaders: MainApiHeaders,
                        @Query("email") email:String,
                        @Query("password") password:String) : Response<ResponseData<User>>

}