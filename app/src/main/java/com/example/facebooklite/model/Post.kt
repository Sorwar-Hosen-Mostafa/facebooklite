package com.example.facebooklite.model

import android.os.Parcel
import android.os.Parcelable


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
):Parcelable {
     constructor(parcel: Parcel) : this(
          parcel.readLong(),
          parcel.readString()!!,
          parcel.readString()!!,
          parcel.readString()!!,
          parcel.readInt(),
          parcel.readInt(),
          parcel.readByte() != 0.toByte(),
          parcel.readString(),
          parcel.readString()
     ) {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeLong(id)
          parcel.writeString(actorName)
          parcel.writeString(title)
          parcel.writeString(content)
          parcel.writeInt(likesCount)
          parcel.writeInt(commentsCount)
          parcel.writeByte(if (isLiked) 1 else 0)
          parcel.writeString(postImageUrl)
          parcel.writeString(actorImageUrl)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<Post> {
          override fun createFromParcel(parcel: Parcel): Post {
               return Post(parcel)
          }

          override fun newArray(size: Int): Array<Post?> {
               return arrayOfNulls(size)
          }
     }
}