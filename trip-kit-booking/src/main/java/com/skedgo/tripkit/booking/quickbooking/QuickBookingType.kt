package com.skedgo.tripkit.booking.quickbooking

import androidx.annotation.StringDef

@Retention(AnnotationRetention.RUNTIME)
@StringDef(
        QuickBookingType.MULTIPLE_CHOICE,
        QuickBookingType.SINGLE_CHOICE,
        QuickBookingType.LONG_TEXT,
        QuickBookingType.RETURN_TRIP
)
annotation class QuickBookingType {
    companion object {
        const val MULTIPLE_CHOICE = "MULTIPLE_CHOICE"
        const val SINGLE_CHOICE = "SINGLE_CHOICE"
        const val LONG_TEXT = "LONG_TEXT"
        const val RETURN_TRIP = "RETURN_TRIP"
    }
}