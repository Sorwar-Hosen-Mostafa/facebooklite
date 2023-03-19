package com.example.facebooklite.model

import android.os.Parcel
import android.os.Parcelable

data class User (
     val id: Long = 0,
     val name: String? = null,
     val email: String? = null,
     val phone: String? = null,
     val address: String? = null,
     val photo_url : String? = null
): Parcelable {
     constructor(parcel: Parcel) : this(
          parcel.readLong(),
          parcel.readString(),
          parcel.readString(),
          parcel.readString(),
          parcel.readString(),
          parcel.readString()
     ) {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeLong(id)
          parcel.writeString(name)
          parcel.writeString(email)
          parcel.writeString(phone)
          parcel.writeString(address)
          parcel.writeString(photo_url)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<User> {
          override fun createFromParcel(parcel: Parcel): User {
               return User(parcel)
          }

          override fun newArray(size: Int): Array<User?> {
               return arrayOfNulls(size)
          }
     }

}