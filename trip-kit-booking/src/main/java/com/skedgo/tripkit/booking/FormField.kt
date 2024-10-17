package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName
import java.io.Serializable

abstract class FormField() : Parcelable, Serializable {

    companion object {
        const val STRING = 1
        const val OPTION = 2
        const val DATETIME = 3
        const val STEPPER = 4
        const val ADDRESS = 5
        const val LINK = 6
        const val BOOKINGFORM = 7
        const val SWITCH = 8
        const val PASSWORD = 9
        const val EXTERNAL = 10
        const val QRCODE = 11

        const val ACCESS_TOKEN = "access_token"
        const val EXPIRES_IN = "expires_in"
        const val REFRESH_TOKEN = "refresh_token"
        const val REDIRECT_URI = "redirectUri"
        const val CLIENT_ID = "clientID"
        const val CLIENT_SECRET = "clientSecret"
        const val AUTH_URL = "authURL"
        const val TOKEN_URL = "tokenURL"
        const val SCOPE = "scope"

        @JvmField
        val CREATOR: Parcelable.Creator<FormField> = object : Parcelable.Creator<FormField> {
            override fun createFromParcel(parcel: Parcel): FormField? {
                return when (parcel.readInt()) {
                    PASSWORD -> PasswordFormField(parcel)
                    STRING -> StringFormField(parcel)
                    OPTION -> OptionFormField(parcel)
                    DATETIME -> DateTimeFormField(parcel)
                    STEPPER -> StepperFormField(parcel)
                    LINK -> LinkFormField(parcel)
                    ADDRESS -> AddressFormField(parcel)
                    BOOKINGFORM -> BookingForm(parcel)
                    SWITCH -> SwitchFormField(parcel)
                    QRCODE -> QrFormField(parcel)
                    else -> null
                }
            }

            override fun newArray(size: Int): Array<FormField?> {
                return arrayOfNulls(size)
            }
        }
    }

    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("subtitle")
    var subtitle: String? = null

    @SerializedName("sidetitle")
    var sidetitle: String? = null

    @SerializedName("required")
    var required: Boolean = false

    @SerializedName("readOnly")
    var readOnly: Boolean = false

    @SerializedName("hidden")
    var hidden: Boolean = false

    @SerializedName("type")
    var type: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        title = parcel.readString()
        subtitle = parcel.readString()
        sidetitle = parcel.readString()
        required = parcel.readInt() == 1
        readOnly = parcel.readInt() == 1
        hidden = parcel.readInt() == 1
        type = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(title)
        dest.writeString(subtitle)
        dest.writeString(sidetitle)
        dest.writeInt(if (required) 1 else 0)
        dest.writeInt(if (readOnly) 1 else 0)
        dest.writeInt(if (hidden) 1 else 0)
        dest.writeString(type)
    }

    abstract fun getValue(): Any?
}
