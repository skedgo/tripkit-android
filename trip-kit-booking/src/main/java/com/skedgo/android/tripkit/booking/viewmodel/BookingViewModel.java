package com.skedgo.android.tripkit.booking.viewmodel;

import android.os.Parcelable;

import com.skedgo.android.tripkit.booking.InputForm;
import com.skedgo.android.tripkit.booking.model.BookingForm;
import com.skedgo.android.tripkit.booking.model.LinkFormField;

import rx.Observable;

public interface BookingViewModel {
  Observable<BookingForm> bookingForm();
  Observable<Param> nextBookingForm();
  Observable<BookingForm> loadForm(Param param);
  Observable<Boolean> isDone();
  void observeAuthentication(AuthenticationViewModel authenticationViewModel);
  Observable<Boolean> performAction(BookingForm bookingForm);
  Observable<Boolean> performAction(LinkFormField linkFormField);
  Observable<Boolean> isFetching();

  interface Param extends Parcelable {
    String getMethod();
    String getUrl();
    String getHudText();
    InputForm postBody();
  }
}