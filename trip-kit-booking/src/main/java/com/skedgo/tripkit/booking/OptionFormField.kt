package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class OptionFormField : FormField {

    @SerializedName("value")
    var mValue: OptionValue? = null

    @SerializedName("allValues")
    var allValues: List<OptionValue> = ArrayList()
    constructor(parcel: Parcel) : super( parcel) {  // Call the parent class constructor
        mValue = parcel.readParcelable(OptionValue::class.java.classLoader)
        allValues = parcel.createTypedArrayList(OptionValue.CREATOR) ?: ArrayList()
    }

    // Secondary empty constructor (the default constructor)
    constructor() : super() {
        // Initialize any default values or leave it empty
    }

    override fun getValue(): OptionValue? {
        return mValue
    }

    fun setValue(value: OptionValue) {
        this.mValue = value
    }

    fun getSelectedIndex(): Int {
        mValue?.let { currentValue ->
            if (allValues.isNotEmpty()) {
                for (i in allValues.indices) {
                    if (allValues[i].value == currentValue.value) {
                        return i
                    }
                }
            }
        }
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(OPTION)
        super.writeToParcel(dest, flags)
        dest.writeParcelable(mValue, flags)
        dest.writeTypedList(allValues)
    }

    companion object CREATOR : Parcelable.Creator<OptionFormField> {
        override fun createFromParcel(parcel: Parcel): OptionFormField {
            parcel.readInt() // Read OPTION type
            return OptionFormField(parcel)
        }

        override fun newArray(size: Int): Array<OptionFormField?> {
            return arrayOfNulls(size)
        }
    }

    // Nested OptionValue class
    class OptionValue() : Parcelable {

        @SerializedName("title")
        var title: String? = null

        @SerializedName("value")
        var value: String? = null

        constructor(parcel: Parcel) : this() {
            title = parcel.readString()
            value = parcel.readString()
        }

        constructor(title: String, value: String) : this() {
            this.title = title
            this.value = value
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(title)
            dest.writeString(value)
        }

        companion object CREATOR : Parcelable.Creator<OptionValue> {
            override fun createFromParcel(parcel: Parcel): OptionValue {
                return OptionValue(parcel)
            }

            override fun newArray(size: Int): Array<OptionValue?> {
                return arrayOfNulls(size)
            }
        }
    }
}