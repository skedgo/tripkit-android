package com.skedgo.tripkit.booking.quickbooking

data class PaymentIntent(
    val clientSecret: String,
    val paymentIntentID: String,
    val url: String
)