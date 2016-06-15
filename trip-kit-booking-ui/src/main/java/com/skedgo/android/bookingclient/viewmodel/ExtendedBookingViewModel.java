package com.skedgo.android.bookingclient.viewmodel;

import android.support.annotation.NonNull;

import com.skedgo.android.common.rx.Var;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.LinkFormField;
import com.skedgo.android.tripkit.booking.viewmodel.AuthenticationViewModel;
import com.skedgo.android.tripkit.booking.viewmodel.BookingViewModel;
import com.uservoice.uservoicesdk.Config;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

public class ExtendedBookingViewModel implements BookingViewModel {
  private final Var<Boolean> isCollectingFeedback = Var.create();
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

  public Observable<Boolean> isCollectingFeedback() {
    return isCollectingFeedback.observe();
  }

  public Observable<Config> reportProblem(CollectBookingFeedbackCommand collectBookingFeedback,
                                          CollectBookingFeedbackCommand.Param param) {
    return collectBookingFeedback
        .executeAsync(param)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnRequest(new Action1<Long>() {
          @Override
          public void call(Long unused) {
            isCollectingFeedback.put(true);
          }
        })
        .doOnTerminate(new Action0() {
          @Override
          public void call() {
            isCollectingFeedback.put(false);
          }
        });
  }
}