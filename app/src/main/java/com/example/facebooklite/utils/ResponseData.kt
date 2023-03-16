package com.example.facebooklite.utils

data class ResponseData<T> (
    var data: T? = null,
    var message: String? = null,
    var code: Int? = null
)