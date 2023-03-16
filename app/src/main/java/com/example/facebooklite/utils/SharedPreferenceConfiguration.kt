package com.example.facebooklite.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.facebooklite.model.User
import com.example.facebooklite.utils.SharedPreferenceConfiguration.Companion.KEY_USER_INFO
import com.google.gson.Gson

class SharedPreferenceConfiguration(context: Context) {
    private val mSharedPreferences: SharedPreferences
    private val mEditor: SharedPreferences.Editor
    private val mGson = Gson()


    init {
        mSharedPreferences = context
            .getSharedPreferences(
                "myPref",
                Context.MODE_PRIVATE
            )
        mEditor = mSharedPreferences.edit()
        mEditor.apply()
    }

    fun putString(key: String?, value: String?) {
        mEditor.putString(key, value)
        mEditor.apply()
    }

    fun getString(key: String?): String? {
        return mSharedPreferences.getString(key, "")
    }

    fun putBoolean(key: String?, value: Boolean) {
        mEditor.putBoolean(key, value)
        mEditor.apply()
    }

    fun getBoolean(key: String?): Boolean {
        return mSharedPreferences.getBoolean(key, false);
    }

    val userInfo: User?
        get() = mGson.fromJson(mSharedPreferences.getString(KEY_USER_INFO, ""), User::class.java)

    fun setUserInfo(poInfo: User?) {
        mEditor.putString(KEY_USER_INFO, mGson.toJson(poInfo))
        mEditor.apply()
    }

    companion object {
        private const val KEY_USER_INFO = "UserInfo"
        const val KEY_IS_LOGGED_IN = "isLoggedId"
        private var sSharedPreferenceHelper: SharedPreferenceConfiguration? = null
        fun getInstance(context: Context): SharedPreferenceConfiguration {
            if (sSharedPreferenceHelper == null) {
                sSharedPreferenceHelper = SharedPreferenceConfiguration(context)
            }
            return sSharedPreferenceHelper!!
        }
    }
}