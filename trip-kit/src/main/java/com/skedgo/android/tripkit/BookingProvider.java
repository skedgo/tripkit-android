package com.skedgo.android.tripkit;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
    BookingResolver.UBER,
    BookingResolver.LYFT,
    BookingResolver.FLITWAYS,
    BookingResolver.GOCATCH,
    BookingResolver.SMS,
    BookingResolver.OTHERS
})
@Retention(RetentionPolicy.SOURCE)
public @interface BookingProvider {}