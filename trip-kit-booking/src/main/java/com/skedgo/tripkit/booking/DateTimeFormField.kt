package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class DateTimeFormField : FormField {

    @SerializedName("value")
    var mValue: Long = 0

    constructor(parcel: Parcel) : super( parcel) {  // Call the parent class constructor
        mValue = parcel.readLong()
    }

    // Secondary empty constructor (the default constructor)
    constructor() : super() {
        // Initialize any default values or leave it empty
    }

    override fun getValue(): Long {
        return mValue
    }

    fun setValue(value: Long) {
        this.mValue = value
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(DATETIME)
        super.writeToParcel(dest, flags)
        dest.writeLong(mValue)
    }

    companion object CREATOR : Parcelable.Creator<DateTimeFormField> {
        override fun createFromParcel(parcel: Parcel): DateTimeFormField {
            parcel.readInt()  // Read DATETIME
            return DateTimeFormField(parcel)
        }

        override fun newArray(size: Int): Array<DateTimeFormField?> {
            return arrayOfNulls(size)
        }
    }
}
