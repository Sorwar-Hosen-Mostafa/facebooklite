package com.example.facebooklite.model

data class Like (
     val id: Long? = null,
     val postId: Long,
     val actorId: Long,
     val actorName: String,
     val actorImageUrl: String,
)