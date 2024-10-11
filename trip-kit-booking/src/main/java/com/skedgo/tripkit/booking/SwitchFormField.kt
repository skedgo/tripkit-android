package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

class SwitchFormField : FormField {

    @SerializedName("keyboardType")
    var keyboardType: String? = null

    @SerializedName("value")
    var mValue: Boolean? = null

    constructor(parcel: Parcel) : super( parcel) {  // Call the parent class constructor
        keyboardType = parcel.readString()  // Read String for keyboardType
        mValue = parcel.readString()?.toBoolean()
    }

    // Secondary empty constructor (the default constructor)
    constructor() : super() {
        // Initialize any default values or leave it empty
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(SWITCH)
        super.writeToParcel(dest, flags)
        dest.writeString(keyboardType)
        dest.writeString(mValue.toString())
    }

    override fun getValue(): Boolean? {
        return mValue
    }

    fun setValue(value: Boolean) {
        this.mValue = value
    }

    companion object CREATOR : Creator<SwitchFormField> {
        override fun createFromParcel(parcel: Parcel): SwitchFormField {
            parcel.readInt() // Read SWITCH type
            return SwitchFormField(parcel)
        }

        override fun newArray(size: Int): Array<SwitchFormField?> {
            return arrayOfNulls(size)
        }
    }
}
