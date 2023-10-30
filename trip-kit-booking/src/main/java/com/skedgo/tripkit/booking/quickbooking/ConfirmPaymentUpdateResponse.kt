package com.skedgo.tripkit.booking.quickbooking

data class ConfirmPaymentUpdateResponse(
    val updateURL: String? = null,
    val paymentIntentID: String? = null,
    val clientSecret: String? = null,
    val url: String? = null,
)