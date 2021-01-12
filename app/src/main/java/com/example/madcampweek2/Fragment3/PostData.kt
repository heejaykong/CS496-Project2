package com.example.madcampweek2.Fragment3

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

class PostData(
    var id : String?,
    var name : String?,
    var profileUrl : String?,
    var uploadTime : String?,
    var text: String?,
    var photoUrl: String?,
    var number: Int,
    var isMe : Boolean

) : Parcelable, Comparable<PostData> {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readBoolean()
    )
    override fun describeContents() = 0
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(dest: Parcel?, flags: Int): Unit = with(dest) {
        this?.writeString(id)
        this?.writeString(name)
        this?.writeString(profileUrl)
        this?.writeString(uploadTime)
        this?.writeString(text)
        this?.writeString(photoUrl)
        this?.writeInt(number)
        this?.writeBoolean(isMe)
    }

    companion object CREATOR : Parcelable.Creator<PostData> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): PostData {
            return PostData(parcel)
        }

        override fun newArray(size: Int): Array<PostData?> {
            return arrayOfNulls(size)
        }
    }

    override fun compareTo(other: PostData): Int {
        return compareValues(this.uploadTime, other.uploadTime)
    }
}