package com.skedgo.android.bookingclient.viewmodel;

import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.tripkit.booking.BookingError;

import java.io.StringReader;

public final class BookingErrorViewModel implements IBookingErrorViewModel {
    private final Gson gson;
    private final String defaultErrorTitle;
    private BookingError bookingError;

    public BookingErrorViewModel(@NonNull Gson gson, @NonNull String defaultErrorTitle) {
        this.gson = gson;
        this.defaultErrorTitle = defaultErrorTitle;
    }

    public void read(String bookingErrorJson) {
        try {
            final JsonReader jsonReader = new JsonReader(new StringReader(bookingErrorJson));
            bookingError = gson.fromJson(jsonReader, BookingError.class);
        } catch (Exception e) {
            // In case we make any unpredicted mistake.
            Crashlytics.logException(e);
        }
    }

    @Override
    public String getErrorTitle() {
        if (bookingError == null || bookingError.getTitle() == null) {
            return defaultErrorTitle;
        } else {
            return bookingError.getTitle();
        }
    }

    @Override
    public String getErrorMessage() {
        if (bookingError == null) {
            return null;
        } else {
            return bookingError.getError();
        }
    }
}