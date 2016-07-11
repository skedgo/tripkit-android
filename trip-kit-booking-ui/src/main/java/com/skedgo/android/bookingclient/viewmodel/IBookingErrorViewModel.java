package com.skedgo.android.bookingclient.viewmodel;

public interface IBookingErrorViewModel {
    void read(String bookingErrorJson);
    String getErrorTitle();
    String getErrorMessage();
}