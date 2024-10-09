package com.skedgo.tripkit.booking

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class BookingErrorData(
    @SerializedName("title")
    var title: String? = null,

    @SerializedName("errorCode")
    var errorCode: Int = 0,

    @SerializedName("error")
    var error: String? = null,

    @SerializedName("usererror")
    var hasUserError: Boolean = false,

    @SerializedName("recovery")
    var recovery: String? = null,

    @SerializedName("recoveryTitle")
    var recoveryTitle: String? = null,

    @SerializedName("url")
    var url: String? = null
)

// Custom Exception class that takes a JSON string as input
class BookingError(val errorData: BookingErrorData) : Throwable(errorData.error) {
    companion object {
        // Factory method to parse JSON and create the exception
        fun fromJson(jsonData: String): BookingError {
            val errorData = Gson().fromJson(jsonData, BookingErrorData::class.java)
            return BookingError(errorData)
        }
    }

    val title: String?
        get() {
            return errorData.title
        }

    val errorCode: Int
        get() {
            return errorData.errorCode
        }

    val error: String?
        get() {
            return errorData.error
        }

    val hasUserError: Boolean
        get() {
            return errorData.hasUserError
        }

    val recovery: String?
        get() {
            return errorData.recovery
        }

    val recoveryTitle: String?
        get() {
            return errorData.recoveryTitle
        }

    val url: String?
        get() {
            return errorData.url
        }
}
