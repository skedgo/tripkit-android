package com.skedgo.tripkit.booking.quickbooking

data class QuickBooking(
        val bookingTitle: String,
        val bookingURL: String,
        val bookingURLIsDeepLink: Boolean,
        val count: Int,
        val index: Int,
        val input: List<Input>,
        val title: String,
        val tripUpdateURL: String
)

data class Option(
        val id: String,
        val title: String
)

data class Input(
        val id: String,
        val options: List<Option>?,
        val required: Boolean,
        val title: String,
        val type: String,
        var value: String?,
        var values: List<String>?
)