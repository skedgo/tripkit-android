package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

class BookingError() : Throwable(), Parcelable {

    @SerializedName("title")
    var title: String? = null

    @SerializedName("errorCode")
    var errorCode: Int = 0

    @SerializedName("error")
    var error: String? = null

    @SerializedName("usererror")
    var hasUserError: Boolean = false

    @SerializedName("recovery")
    var recovery: String? = null

    @SerializedName("recoveryTitle")
    var recoveryTitle: String? = null

    @SerializedName("url")
    var url: String? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        errorCode = parcel.readInt()
        error = parcel.readString()
        hasUserError = parcel.readInt() == 1
        recovery = parcel.readString()
        recoveryTitle = parcel.readString()
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(errorCode)
        parcel.writeString(error)
        parcel.writeInt(if (hasUserError) 1 else 0)
        parcel.writeString(recovery)
        parcel.writeString(recoveryTitle)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookingError> {
        override fun createFromParcel(parcel: Parcel): BookingError {
            return BookingError(parcel)
        }

        override fun newArray(size: Int): Array<BookingError?> {
            return arrayOfNulls(size)
        }
    }
}
