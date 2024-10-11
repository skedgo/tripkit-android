package com.skedgo.tripkit.booking

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName
import java.util.UUID

open class BookingForm() : FormField() {

    @SerializedName("action")
    var action: BookingAction? = null

    @SerializedName("form")
    var form: List<FormGroup> = listOf()

    @SerializedName("value")
    var mValue: String? = null

    @SerializedName("refreshURLForSourceObject")
    var refreshURLForSourceObject: String? = null

    @SerializedName("image")
    var imageUrl: String? = null

    constructor(parcel: Parcel) : this() {
        action = parcel.readParcelable(BookingAction::class.java.classLoader)
        form = mutableListOf<FormGroup>().apply {
            parcel.readTypedList(this, FormGroup.CREATOR)
        }
        mValue = parcel.readString()
        refreshURLForSourceObject = parcel.readString()
        imageUrl = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(BOOKINGFORM)
        super.writeToParcel(dest, flags)
        dest.writeParcelable(action, flags)
        dest.writeTypedList(form)
        dest.writeString(mValue)
        dest.writeString(refreshURLForSourceObject)
        dest.writeString(imageUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Creator<BookingForm> {
        override fun createFromParcel(parcel: Parcel): BookingForm {
            parcel.readInt()
            return BookingForm(parcel)
        }

        override fun newArray(size: Int): Array<BookingForm?> = arrayOfNulls(size)
    }

    override fun getValue(): Any? {
        return mValue
    }

    fun isOAuthForm(): Boolean = "authForm" == type

    fun isSpecificAuthForm(): Boolean = mValue == "payiq" || mValue == "myki"

    fun isUberAuthForm(): Boolean = mValue == "uber"

    fun getClientID(): String? = getValueFromField(FormField.CLIENT_ID)

    fun getClientSecret(): String? = getValueFromField(FormField.CLIENT_SECRET)

    fun getAuthURL(): String? = getValueFromField(FormField.AUTH_URL)

    fun getToken(): String? = getValueFromField(FormField.ACCESS_TOKEN)

    fun getExpiresIn(): Int = getValueFromField(FormField.EXPIRES_IN)?.toIntOrNull() ?: 0

    fun getRefreshToken(): String? = getValueFromField(FormField.REFRESH_TOKEN)

    fun getTokenURL(): String? {
        form.forEach { formGroup ->
            formGroup.fields.forEach { formField ->
                if (FormField.TOKEN_URL == formField.id && formField.getValue() != null && formField.getValue().toString().endsWith("/token")) {
                    return formField.getValue().toString().substring(0, formField.getValue().toString().length - "/token".length)
                }
            }
        }
        return null
    }

    fun getScope(): String? {
        form.forEach { formGroup ->
            formGroup.fields.forEach { formField ->
                if (FormField.SCOPE == formField.id && formField.getValue() != null) {
                    return formField.getValue().toString()
                }
            }
        }
        return null
    }

    fun getOAuthLink(): Uri? {
        if (isOAuthForm()) {
            val authURL = getAuthURL()
            val clientID = getClientID()
            val scope = getScope()

            if (authURL != null && clientID != null && scope != null) {
                val builder = Uri.parse(authURL).buildUpon()
                    .appendQueryParameter("client_id", clientID)
                    .appendQueryParameter("response_type", "code")
                    .appendQueryParameter("state", UUID.randomUUID().toString())
                    .appendQueryParameter("redirect_uri", getValueFromField(FormField.REDIRECT_URI))

                if (!scope.isNullOrEmpty()) {
                    builder.appendQueryParameter("scope", scope)
                }

                return builder.build()
            }
        }
        return null
    }

    fun setAuthData(externalOAuth: ExternalOAuth): BookingForm {
        form.forEach { formGroup ->
            formGroup.fields.forEach { formField ->
                when (formField.id) {
                    FormField.ACCESS_TOKEN -> (formField as? StringFormField)?.setValue(externalOAuth.token())
                    FormField.EXPIRES_IN -> (formField as? StringFormField)?.setValue(externalOAuth.expiresIn().toString())
                    FormField.REFRESH_TOKEN -> externalOAuth.refreshToken()?.let {
                        (formField as? StringFormField)?.setValue(it)
                    }
                }
            }
        }
        return this
    }

    fun externalAction(): String? {
        form.forEach { group ->
            group.fields.forEach { field ->
                if (field is LinkFormField && LinkFormField.METHOD_EXTERNAL == field.method) {
                    return field.getValue()
                }
            }
        }
        return null
    }

    private fun getValueFromField(fieldName: String): String? {
        form.forEach { formGroup ->
            formGroup.fields.forEach { formField ->
                if (fieldName == formField.id && formField.getValue() != null) {
                    return formField.getValue().toString()
                }
            }
        }
        return null
    }
}