package com.example.facebooklite.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User (
     val id: Long = 0,
     val name: String? = null,
     val email: String? = null,
     val phone: String? = null,
     val address: String? = null,
     val photo_url : String? = null,
     val createDate: Date? = null
): Parcelable