package com.example.madcampweek2.Fragment3

import android.os.Parcel
import android.os.Parcelable

class PostData(
    var id : String?,
    val photoURI : String?,
    val name: String?,
    val number: String?
) : Parcelable, Comparable<PostData> {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )
    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel?, flags: Int): Unit = with(dest) {
        this?.writeString(id)
        this?.writeString(photoURI)
        this?.writeString(name)
        this?.writeString(number)
    }

    companion object CREATOR : Parcelable.Creator<PostData> {
        override fun createFromParcel(parcel: Parcel): PostData {
            return PostData(parcel)
        }

        override fun newArray(size: Int): Array<PostData?> {
            return arrayOfNulls(size)
        }
    }

    override fun compareTo(other: PostData): Int {
        return compareValues(this.name, other.name)
    }
}