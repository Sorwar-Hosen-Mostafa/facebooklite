package com.example.facebooklite.data.remote.header

import android.content.Context
import com.example.facebooklite.MainApiHeaders
import com.example.facebooklite.utils.SharedPreferenceConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ApiMainHeadersProvider @Inject constructor(@ApplicationContext private val appContext: Context){

    fun getAuthenticatedHeaders(): MainApiHeaders =
        MainApiHeaders().apply {
            SharedPreferenceConfiguration.getInstance(appContext).userInfo?.let {
                put(USER_ID, it.id.toString())
            }

        }

    companion object {
        private const val USER_ID = "user_id"
    }
}