package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

class QrFormField : FormField {

    @SerializedName("value")
    var mValue: String? = null

    constructor(parcel: Parcel) : super( parcel) {  // Call the parent class constructor
        mValue = parcel.readString()
    }

    // Secondary empty constructor (the default constructor)
    constructor() : super() {
        // Initialize any default values or leave it empty
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(QRCODE)
        super.writeToParcel(dest, flags)
        dest.writeString(mValue)
    }

    override fun getValue(): String? {
        return mValue
    }

    fun setValue(value: String) {
        this.mValue = value
    }

    companion object CREATOR : Creator<QrFormField> {
        override fun createFromParcel(parcel: Parcel): QrFormField {
            parcel.readInt() // Read QRCODE type
            return QrFormField(parcel)
        }

        override fun newArray(size: Int): Array<QrFormField?> {
            return arrayOfNulls(size)
        }
    }
}
