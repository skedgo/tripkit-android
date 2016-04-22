package com.skedgo.android.bookingkit.viewmodel;

import android.os.Parcelable;

import com.skedgo.android.bookingkit.model.BookingForm;
import com.skedgo.android.bookingkit.model.InputForm;
import com.skedgo.android.bookingkit.model.LinkFormField;

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