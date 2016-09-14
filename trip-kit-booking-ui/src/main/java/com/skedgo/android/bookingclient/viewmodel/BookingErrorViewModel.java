package com.skedgo.android.bookingclient.viewmodel;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.skedgo.android.tripkit.booking.BookingError;

public final class BookingErrorViewModel {
  private static final String TAG = "bookingclient";
  private final String defaultErrorTitle;
  private BookingError bookingError;

  public BookingErrorViewModel(@NonNull String defaultErrorTitle) {
    this.defaultErrorTitle = defaultErrorTitle;
  }

  public void setBookingError(BookingError bookingError) {
    this.bookingError = bookingError;
  }

  public String getErrorTitle() {
    if (bookingError == null || bookingError.getTitle() == null) {
      return defaultErrorTitle;
    } else {
      return bookingError.getTitle();
    }
  }

  public String getErrorMessage() {
    if (bookingError == null) {
      return null;
    } else {
      return bookingError.getError();
    }
  }
}