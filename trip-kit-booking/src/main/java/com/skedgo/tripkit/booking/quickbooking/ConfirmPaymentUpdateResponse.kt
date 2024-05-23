package com.skedgo.tripkit.booking.quickbooking

import com.google.gson.annotations.SerializedName

data class ConfirmPaymentUpdateResponse(
    val updateURL: String? = null,
    val paymentIntentID: String? = null,
    val clientSecret: String? = null,
    val url: String? = null,
    val warning: String? = null,
)

data class ConfirmPaymentError(
    val errorCode: Int,
    val title: String,
    val error: String,
    @SerializedName("usererror") val userError: Boolean
)