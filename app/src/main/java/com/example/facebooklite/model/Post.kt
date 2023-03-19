package com.example.facebooklite.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post(
    val id: Long,
    val actorName: String,
    val title: String,
    val content: String,
    var likesCount: Int,
    var commentsCount: Int,
    var liked: Boolean,
    val postImageUrl: String? = null,
    val actorImageUrl: String? = null,
    val createDate: Date
): Parcelable