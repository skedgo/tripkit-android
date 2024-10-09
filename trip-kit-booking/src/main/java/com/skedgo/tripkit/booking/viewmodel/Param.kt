package com.skedgo.tripkit.booking.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.skedgo.tripkit.booking.BookingAction
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.InputForm
import com.skedgo.tripkit.booking.LinkFormField

data class Param(
    val url: String?,
    val method: String?,
    val hudText: String?,
    val postBody: InputForm?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(InputForm::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(hudText)
        parcel.writeString(method)
        parcel.writeParcelable(postBody, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Param> {
        override fun createFromParcel(parcel: Parcel): Param {
            return Param(parcel)
        }

        override fun newArray(size: Int): Array<Param?> {
            return arrayOfNulls(size)
        }

        fun create(url: String): Param {
            return Param(url, LinkFormField.METHOD_GET, null, null)
        }

        fun create(bookingAction: BookingAction, postBody: InputForm): Param {
            return Param(bookingAction.url, LinkFormField.METHOD_POST, bookingAction.hudText, postBody)
        }

        fun create(linkFormField: LinkFormField): Param {
            val postBody = if (linkFormField.method == LinkFormField.METHOD_POST) InputForm() else null
            return Param(linkFormField.value, linkFormField.method, null, postBody)
        }

        fun create(form: BookingForm): Param {
            val postBody = InputForm.from(form.form)
            return Param(form.action?.url, LinkFormField.METHOD_POST, null, postBody)
        }
    }
}