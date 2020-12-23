package com.example.lib_base.bean

import android.os.Parcel
import android.os.Parcelable

/**
Created by chene on @date 20-12-10 下午5:13
 **/
class User(
    val admin: Boolean,
//    val chapterTops: List<Any>,
    val coinCount: Int,
//    val collectIds: List<Int>,
    val email: String?,
    val icon: String?,
    val id: Int,
    val nickname: String?,
    val password: String?,
    val publicName: String?,
    val token: String?,
    val type: Int,
    val username: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (admin) 1 else 0)
        parcel.writeInt(coinCount)
        parcel.writeString(email)
        parcel.writeString(icon)
        parcel.writeInt(id)
        parcel.writeString(nickname)
        parcel.writeString(password)
        parcel.writeString(publicName)
        parcel.writeString(token)
        parcel.writeInt(type)
        parcel.writeString(username)
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