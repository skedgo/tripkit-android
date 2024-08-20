package com.skedgo.tripkit.bookingproviders;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

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
public @interface BookingProvider {
}