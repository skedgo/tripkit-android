package com.skedgo.tripkit.bookingproviders

import androidx.annotation.IntDef
import kotlin.annotation.AnnotationRetention.SOURCE

@IntDef(
    BookingResolver.UBER,
    BookingResolver.LYFT,
    BookingResolver.FLITWAYS,
    BookingResolver.GOCATCH,
    BookingResolver.INGOGO,
    BookingResolver.MTAXI,
    BookingResolver.SMS,
    BookingResolver.OTHERS
)
@Retention(SOURCE)
annotation class BookingProvider 