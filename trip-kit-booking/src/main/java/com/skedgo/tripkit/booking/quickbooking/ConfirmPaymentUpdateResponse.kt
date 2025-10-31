package com.skedgo.tripkit.booking.quickbooking

import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.booking.confirmation.BookingConfirmationAction

data class ConfirmPaymentUpdateResponse(
    val updateURL: String? = null,
    val paymentIntentID: String? = null,
    val clientSecret: String? = null,
    val url: String? = null,
    val warning: String? = null,
    val actionRequired: ActionRequired? = null
)

data class ConfirmPaymentError(
    val errorCode: Int,
    val title: String,
    val error: String,
    @SerializedName("usererror") val userError: Boolean
)

data class ActionRequired(
    val actions: List<BookingConfirmationAction>,
    val differences: List<Difference>,
    val message: String,
    val title: String
) {
    data class Difference(
        val bookingType: String,
        val externalFrom: Location?,
        val externalTo: Location?,
        val from: Location,
        val to: Location
    ) {

        data class Location(
            val address: String,
            val lat: Double,
            val lng: Double
        )
    }
}