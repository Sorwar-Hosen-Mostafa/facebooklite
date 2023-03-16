package com.example.facebooklite

data class ResponseData<T> (
    var data: T? = null,
    var message: String? = null,
    var code: Int? = null
)