package com.example.facebooklite.model


data class Post (
     val id: Long,
     val userInfo: User,
     val title: String,
     val content: String,
     val likes: List<Like>,
     val comments: List<Comment>,
     val postImageUrl: String? = null,
)