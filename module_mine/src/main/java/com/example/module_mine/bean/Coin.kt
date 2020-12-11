package com.example.module_mine.bean

import android.os.Parcel
import android.os.Parcelable

/**
Created by chene on @date 20-12-10 下午11:32
 **/
data class Coin(
    val coinCount: Int,
    val rank: Int,
    val userId: Int,
    val userName: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(coinCount)
        parcel.writeInt(rank)
        parcel.writeInt(userId)
        parcel.writeString(userName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coin> {
        override fun createFromParcel(parcel: Parcel): Coin {
            return Coin(parcel)
        }

        override fun newArray(size: Int): Array<Coin?> {
            return arrayOfNulls(size)
        }
    }
}