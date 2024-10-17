package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

class StringFormField : FormField {

    @SerializedName("keyboardType")
    var keyboardType: String? = null

    @SerializedName("value")
    var mValue: String? = null
    constructor(parcel: Parcel) : super( parcel) {  // Call the parent class constructor
        keyboardType = parcel.readString()  // Read String for keyboardType
        mValue = parcel.readString()  // Read String for value
    }

    // Secondary empty constructor (the default constructor)
    constructor() : super() {
        // Initialize any default values or leave it empty
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(STRING)
        super.writeToParcel(dest, flags)
        dest.writeString(keyboardType)
        dest.writeString(mValue)
    }

    override fun getValue(): String? {
        return mValue
    }

    fun setValue(value: String) {
        this.mValue = value
    }

    companion object CREATOR : Creator<StringFormField> {
        override fun createFromParcel(parcel: Parcel): StringFormField {
            parcel.readInt() // Read STRING type
            return StringFormField(parcel)
        }

        override fun newArray(size: Int): Array<StringFormField?> {
            return arrayOfNulls(size)
        }
    }
}
