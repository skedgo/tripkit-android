package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class AddressFormField() : FormField() {

    @SerializedName("value")
    var mValue: Address? = null

    constructor(parcel: Parcel) : this() {
        parcel.readInt() // ADDRESS (or any other constant value you might be using)
        mValue = parcel.readParcelable(Address::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ADDRESS) // ADDRESS constant as in the original code
        super.writeToParcel(parcel, flags)
        parcel.writeParcelable(mValue, flags)
    }

    override fun getValue(): Address? {
        return mValue
    }

    fun setValue(value: Address) {
        this.mValue = value
    }

    companion object CREATOR : Parcelable.Creator<AddressFormField> {
        override fun createFromParcel(parcel: Parcel): AddressFormField {
            parcel.readInt()
            return AddressFormField(parcel)
        }

        override fun newArray(size: Int): Array<AddressFormField?> {
            return arrayOfNulls(size)
        }
    }

    class Address() : Parcelable {
        @SerializedName("lat")
        var latitude: Double = 0.0
        @SerializedName("lng")
        var longitude: Double = 0.0
        @SerializedName("address")
        var address: String? = null
        @SerializedName("name")
        var name: String? = null

        constructor(parcel: Parcel) : this() {
            latitude = parcel.readDouble()
            longitude = parcel.readDouble()
            address = parcel.readString()
            name = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeDouble(latitude)
            parcel.writeDouble(longitude)
            parcel.writeString(address)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Address> {
            override fun createFromParcel(parcel: Parcel): Address {
                return Address(parcel)
            }

            override fun newArray(size: Int): Array<Address?> {
                return arrayOfNulls(size)
            }
        }
    }
}
