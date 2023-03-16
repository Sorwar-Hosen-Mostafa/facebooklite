package com.example.facebooklite.model


data class Post (
     val id: Long,
     val actorName:String,
     val title: String,
     val content: String,
     val likesCount: Int,
     val commentsCount: Int,
     val isLiked: Boolean,
     val postImageUrl: String? = null,
     val actorImageUrl:String? = null
)