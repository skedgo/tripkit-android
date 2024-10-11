package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

class LinkFormField : FormField {

    companion object {
        const val METHOD_GET = "get"
        const val METHOD_POST = "post"
        const val METHOD_REFRESH = "refresh"
        const val METHOD_EXTERNAL = "external"

        @JvmField
        val CREATOR: Parcelable.Creator<LinkFormField> = object : Parcelable.Creator<LinkFormField> {
            override fun createFromParcel(parcel: Parcel): LinkFormField {
                parcel.readInt() // Read LINK type
                return LinkFormField(parcel)
            }

            override fun newArray(size: Int): Array<LinkFormField?> {
                return arrayOfNulls(size)
            }
        }
    }

    @SerializedName("value")
    var link: String? = null

    @SerializedName("method")
    var method: String? = null

    constructor(parcel: Parcel) : super( parcel) {  // Call the parent class constructor
        link = parcel.readString()
        method = parcel.readString()
    }

    // Secondary empty constructor (the default constructor)
    constructor() : super() {
        // Initialize any default values or leave it empty
    }

    override fun getValue(): String? {
        return link
    }

    fun setValue(link: String) {
        this.link = link
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(LINK)
        super.writeToParcel(dest, flags)
        dest.writeString(link)
        dest.writeString(method)
    }
}