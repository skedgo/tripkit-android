package com.skedgo.android.tripkit.bookingproviders;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
    BookingResolver.UBER,
    BookingResolver.LYFT,
    BookingResolver.FLITWAYS,
    BookingResolver.GOCATCH,
    BookingResolver.INGOGO,
    BookingResolver.MTAXI,
    BookingResolver.SMS,
    BookingResolver.OTHERS
})
@Retention(RetentionPolicy.SOURCE)
public @interface BookingProvider {}