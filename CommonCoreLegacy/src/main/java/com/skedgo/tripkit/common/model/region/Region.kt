package com.skedgo.tripkit.common.model.region

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.TextUtils
import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.location.Location

open class Region(
    @SerializedName("cities") var cities: ArrayList<City>? = arrayListOf(),
    @SerializedName("polygon") var encodedPolyline: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("urls") var urls: ArrayList<String>? = arrayListOf(),
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("modes") var transportModeIds: ArrayList<String>? = arrayListOf()
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Region> {
        override fun createFromParcel(source: Parcel): Region {
            return Region(
                name = source.readString(),
                urls = source.createStringArrayList()?.let { ArrayList(it) },
                timezone = source.readString(),
                transportModeIds = source.createStringArrayList()?.let { ArrayList(it) },
                encodedPolyline = source.readString(),
                cities = source.readArrayList(City::class.java.classLoader) as? ArrayList<City>
            )
        }

        override fun newArray(size: Int): Array<Region?> {
            return arrayOfNulls(size)
        }
    }

    @Nullable
    fun getURLs(): ArrayList<String>? {
        return urls
    }

    fun setURLs(urls: ArrayList<String>) {
        this.urls = urls
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Region) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name?.hashCode() ?: 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeStringList(urls)
        dest.writeString(timezone)
        dest.writeStringList(transportModeIds)
        dest.writeString(encodedPolyline)
        dest.writeList(cities)
    }

    /**
     * City class which extends Location
     */
    class City : Location()
}