package com.skedgo.tripkit.booking.viewmodel;

import com.skedgo.tripkit.booking.BookingForm;
import com.skedgo.tripkit.booking.LinkFormField;

import io.reactivex.Flowable;

public interface BookingViewModel {
    Flowable<BookingForm> bookingForm();

    Flowable<Param> nextBookingForm();

    Flowable<BookingForm> loadForm(Param param);

    Flowable<Boolean> isDone();

    void observeAuthentication(AuthenticationViewModel authenticationViewModel);

    Flowable<Boolean> performAction(BookingForm bookingForm);

    Flowable<Boolean> performAction(LinkFormField linkFormField);

    Flowable<Boolean> isFetching();

    Param paramFrom(BookingForm form);

}