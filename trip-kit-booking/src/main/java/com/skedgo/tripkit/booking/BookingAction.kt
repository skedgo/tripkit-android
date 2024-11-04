package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

data class BookingAction(
    @SerializedName("title") var title: String? = null,
    @SerializedName("enabled") var enable: Boolean = true,
    @SerializedName("url") var url: String? = null,
    @SerializedName("hudText") var hudText: String? = null,
    @SerializedName("done") var done: Boolean = false
) : Parcelable {

    private constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        enable = parcel.readString()?.toBoolean() ?: true
        url = parcel.readString()
        hudText = parcel.readString()
        done = parcel.readString()?.toBoolean() ?: false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(enable.toString())
        parcel.writeString(url)
        parcel.writeString(hudText)
        parcel.writeString(done.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookingAction> {
        override fun createFromParcel(parcel: Parcel): BookingAction {
            return BookingAction(parcel)
        }

        override fun newArray(size: Int): Array<BookingAction?> {
            return arrayOfNulls(size)
        }
    }
}
