package com.example.facebooklite.model

data class User (
     val id: Long = 0,
     val name: String? = null,
     val email: String? = null,
     val phone: String? = null,
     val address: String? = null,
     val photo_url : String? = null
)