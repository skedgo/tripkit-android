package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

class FormGroup() : Parcelable {

    @SerializedName("title")
    var title: String? = null

    @SerializedName("footer")
    var footer: String? = null

    @SerializedName("fields")
    var fields: List<FormField> = ArrayList()

    private constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        footer = parcel.readString()
        fields = parcel.createTypedArrayList(FormField.CREATOR) ?: ArrayList()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(footer)
        dest.writeTypedList(fields)
    }

    companion object CREATOR : Parcelable.Creator<FormGroup> {
        override fun createFromParcel(parcel: Parcel): FormGroup {
            return FormGroup(parcel)
        }

        override fun newArray(size: Int): Array<FormGroup?> {
            return arrayOfNulls(size)
        }
    }
}