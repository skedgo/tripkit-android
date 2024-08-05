package com.skedgo.tripkit.booking.quickbooking

import androidx.annotation.StringDef

@Retention(AnnotationRetention.RUNTIME)
@StringDef(
    QuickBookingType.MULTIPLE_CHOICE,
    QuickBookingType.SINGLE_CHOICE,
    QuickBookingType.LONG_TEXT,
    QuickBookingType.RETURN_TRIP,
    QuickBookingType.NUMBER,
    QuickBookingType.TERMS
)
annotation class QuickBookingType {
    companion object {
        const val MULTIPLE_CHOICE = "MULTIPLE_CHOICE"
        const val SINGLE_CHOICE = "SINGLE_CHOICE"
        const val LONG_TEXT = "LONG_TEXT"
        const val RETURN_TRIP = "RETURN_TRIP"
        const val NUMBER = "NUMBER"
        const val TERMS = "TERMS"
    }
}

fun String.getDefaultValueByType(
    title: String,
    values: List<String>? = null,
    defaultActionTitle: String? = null
): String {
    return when (this) {
        QuickBookingType.LONG_TEXT -> {
            String.format("Tap to %s", title)
        }

        QuickBookingType.NUMBER -> {
            values?.firstOrNull() ?: "0"
        }

        else -> {
            "Tap to make selections"
        }
    }
}