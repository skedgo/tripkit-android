package com.skedgo.tripkit.booking.quickbooking

data class QuickBookRequest(
    val input: List<Input>,
    val fares: List<Fare>
)