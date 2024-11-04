package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ExternalFormField : FormField, Serializable {

    @SerializedName("disregardURL")
    var disregardURL: String? = null

    @SerializedName("nextHudText")
    var nextHudText: String? = null

    @SerializedName("nextURL")
    var nextURL: String? = null

    @SerializedName("value")
    var mValue: String? = null
    constructor(parcel: Parcel) : super( parcel) {  // Call the parent class constructor
        disregardURL = parcel.readString()
        nextHudText = parcel.readString()
        nextURL = parcel.readString()
        mValue = parcel.readString()
    }

    // Secondary empty constructor (the default constructor)
    constructor() : super() {
        // Initialize any default values or leave it empty
    }

    override fun getValue(): String? {
        return mValue
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(EXTERNAL)
        super.writeToParcel(dest, flags)
        dest.writeString(disregardURL)
        dest.writeString(nextHudText)
        dest.writeString(nextURL)
        dest.writeString(mValue)
    }

    companion object CREATOR : Creator<ExternalFormField> {
        override fun createFromParcel(parcel: Parcel): ExternalFormField {
            parcel.readInt() // Read EXTERNAL
            return ExternalFormField(parcel)
        }

        override fun newArray(size: Int): Array<ExternalFormField?> {
            return arrayOfNulls(size)
        }
    }
}
