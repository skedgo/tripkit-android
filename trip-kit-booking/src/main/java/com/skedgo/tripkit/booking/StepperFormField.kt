package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

class StepperFormField : FormField {

    @SerializedName("value")
    var mValue: Int = 0

    @SerializedName("minValue")
    var minValue: Int = 0

    @SerializedName("maxValue")
    var maxValue: Int = 0

    constructor(parcel: Parcel) : super( parcel) {  // Call the parent class constructor
        mValue = parcel.readInt()
        minValue = parcel.readInt()
        maxValue = parcel.readInt()
    }

    // Secondary empty constructor (the default constructor)
    constructor() : super() {
        // Initialize any default values or leave it empty
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(STEPPER)
        super.writeToParcel(dest, flags)
        dest.writeInt(mValue)
        dest.writeInt(minValue)
        dest.writeInt(maxValue)
    }

    override fun getValue(): Int {
        return mValue
    }

    fun setValue(value: Int) {
        this.mValue = value
    }

    companion object CREATOR : Creator<StepperFormField> {
        override fun createFromParcel(parcel: Parcel): StepperFormField {
            parcel.readInt() // Read STEPPER type
            return StepperFormField(parcel)
        }

        override fun newArray(size: Int): Array<StepperFormField?> {
            return arrayOfNulls(size)
        }
    }
}