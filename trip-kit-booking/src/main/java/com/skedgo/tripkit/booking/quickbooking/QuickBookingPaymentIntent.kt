package com.skedgo.tripkit.booking.quickbooking

data class QuickBookingPaymentIntent(
    val clientSecret: String,
    val paymentIntentID: String,
    val url: String
)