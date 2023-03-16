package com.example.facebooklite.data.remote.api
import com.example.facebooklite.MainApiHeaders
import com.example.facebooklite.ResponseData
import com.example.facebooklite.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Multipart
    @JvmSuppressWildcards
    @POST(value = "signUp")
    suspend fun signUp(@HeaderMap mainApiHeaders: MainApiHeaders,
                       @PartMap partMap: Map<String, RequestBody>) : Response<ResponseData<User>>

}