package com.skedgo.tripkit.routing

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.routing.ServiceColor

/**
 * @see [Mode Identifiers](http://skedgo.github.io/tripgo-api/site/faq/.mode-identifiers)
 */
// FIXME let's remove Parcelable and maybe migrate to a data class
class ModeInfo : Parcelable {
    @SerializedName("alt")
    var alternativeText: String = ""

    @SerializedName("localIcon")
    var localIconName: String = ""

    @SerializedName("remoteIcon")
    var remoteIconName: String = ""

    @SerializedName("remoteDarkIcon")
    var remoteDarkIconName: String? = null

    @SerializedName("description")
    var description: String = ""

    /**
     * @see [Mode Identifiers](http://skedgo.github.io/tripgo-api/site/faq/.mode-identifiers)
     */
    @SerializedName("identifier")
    var id: String = ""

    @SerializedName("color")
    var color: ServiceColor? = null

    @SerializedName("remoteIconIsTemplate")
    var remoteIconIsTemplate: Boolean = false

    @SerializedName("remoteIconIsBranding")
    var remoteIconIsBranding: Boolean = false


    constructor()

    private constructor(source: Parcel) {
        alternativeText = source.readString().orEmpty()
        localIconName = source.readString().orEmpty()
        remoteIconName = source.readString().orEmpty()
        remoteDarkIconName = source.readString()
        description = source.readString().orEmpty()
        id = source.readString().orEmpty()
        color = source.readParcelable(ServiceColor::class.java.classLoader)
        remoteIconIsTemplate = source.readInt() == 1
        remoteIconIsBranding = source.readInt() == 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(alternativeText)
        dest.writeString(localIconName)
        dest.writeString(remoteIconName)
        dest.writeString(remoteDarkIconName)
        dest.writeString(description)
        dest.writeString(id)
        dest.writeParcelable(color, 0)
        dest.writeInt(if (remoteIconIsTemplate) 1 else 0)
        dest.writeInt(if (remoteIconIsBranding) 1 else 0)
    }

    val modeCompat: VehicleMode?
        get() = VehicleMode.from(localIconName)

    companion object {
        @JvmField
        val CREATOR: Creator<ModeInfo> = object : Creator<ModeInfo> {
            override fun createFromParcel(source: Parcel): ModeInfo {
                return ModeInfo(source)
            }

            override fun newArray(size: Int): Array<ModeInfo?> {
                return arrayOfNulls(0)
            }
        }

        const val MAP_LIST_SIZE_RATIO: Float = 1f
    }
}