package com.skedgo.android.tripkit.booking.ui.viewmodel;

import android.support.annotation.NonNull;

import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.LinkFormField;
import com.skedgo.android.tripkit.booking.viewmodel.AuthenticationViewModel;
import com.skedgo.android.tripkit.booking.viewmodel.BookingViewModel;
import com.skedgo.android.tripkit.booking.viewmodel.Param;

import javax.inject.Inject;

import rx.Observable;

public class ExtendedBookingViewModel implements BookingViewModel {
  private final BookingViewModel coreViewModel;

  @Inject
  public ExtendedBookingViewModel(@NonNull BookingViewModel coreViewModel) {
    this.coreViewModel = coreViewModel;
  }

  @Override
  public Observable<BookingForm> bookingForm() {
    return coreViewModel.bookingForm();
  }

  @Override
  public Observable<Param> nextBookingForm() {
    return coreViewModel.nextBookingForm();
  }

  @Override
  public Observable<BookingForm> loadForm(Param param) {
    return coreViewModel.loadForm(param);
  }

  @Override
  public Observable<Boolean> isDone() {
    return coreViewModel.isDone();
  }

  @Override
  public void observeAuthentication(AuthenticationViewModel authenticationViewModel) {
    coreViewModel.observeAuthentication(authenticationViewModel);
  }

  @Override
  public Observable<Boolean> performAction(BookingForm bookingForm) {
    return coreViewModel.performAction(bookingForm);
  }

  @Override
  public Observable<Boolean> performAction(LinkFormField linkFormField) {
    return coreViewModel.performAction(linkFormField);
  }

  @Override
  public Observable<Boolean> isFetching() {
    return coreViewModel.isFetching();
  }

  @Override public Observable<Boolean> needsAuthentication(BookingForm form) {
    return coreViewModel.needsAuthentication(form);
  }

  @Override public Param paramFrom(BookingForm form) {
    return coreViewModel.paramFrom(form);
  }
}