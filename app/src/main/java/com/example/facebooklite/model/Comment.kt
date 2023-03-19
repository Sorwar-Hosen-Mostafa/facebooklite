package com.example.facebooklite.model

import java.util.*

data class Comment (
     val id: Long? = null,
     val comment: String,
     val postId: Long,
     val actorId: Long = 0,
     val actorName: String? = null,
     val actorImageUrl: String? = null,
     val createDate: Date = Date()
)