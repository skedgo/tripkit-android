package com.skedgo.android.tripkit.booking.viewmodel;

import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.LinkFormField;

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

  /**
   * needsAuthentication: checks if external auth needs authentication.
   * Side efect: toke info is updated if saved.
   */
  Observable<Boolean> needsAuthentication(BookingForm form);
  Param paramFrom(BookingForm form);

}