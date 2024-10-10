package com.skedgo.tripkit.routing

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

class ServiceColor : Parcelable {

    @SerializedName("red")
    var red: Int

    @SerializedName("blue")
    var blue: Int

    @SerializedName("green")
    var green: Int

    // Default constructor
    constructor() : this(0, 0, 0)

    // Constructor with red, green, blue values
    constructor(red: Int, green: Int, blue: Int) {
        this.red = red
        this.green = green
        this.blue = blue
    }

    // Factory method to create from a color Int
    companion object {
        fun fromColor(color: Int): ServiceColor {
            return ServiceColor(Color.red(color), Color.green(color), Color.blue(color))
        }

        @JvmField
        val CREATOR: Parcelable.Creator<ServiceColor> = object : Parcelable.Creator<ServiceColor> {
            override fun createFromParcel(parcel: Parcel): ServiceColor {
                return ServiceColor(parcel)
            }

            override fun newArray(size: Int): Array<ServiceColor?> {
                return arrayOfNulls(size)
            }
        }
    }

    // Constructor for parcelable implementation
    private constructor(parcel: Parcel) {
        red = parcel.readInt()
        blue = parcel.readInt()
        green = parcel.readInt()
    }

    // Method to get the RGB color
    val color: Int
        get() = Color.rgb(red, green, blue)

    // Parcelable methods
    override fun describeContents(): Int {
        return hashCode()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(red)
        dest.writeInt(blue)
        dest.writeInt(green)
    }

    override fun equals(other: Any?): Boolean {
        if (other is ServiceColor) {
            return red == other.red && blue == other.blue && green == other.green
        }
        return false
    }
}