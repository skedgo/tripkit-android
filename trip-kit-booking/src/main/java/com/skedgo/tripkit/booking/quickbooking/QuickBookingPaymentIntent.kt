package com.skedgo.tripkit.booking.quickbooking

data class QuickBookingPaymentIntent(
    val clientSecret: String,
    val paymentIntentID: String,
    val url: String,
    val stripeApiKey: String? = null,
    val updateURL: String? = null,
    val type: String? = null,
    val internalPaymentIntentFetched: Boolean = false
)