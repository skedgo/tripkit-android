package com.skedgo.android.tripkit.booking.viewmodel;

import com.skedgo.android.common.rx.Var;

import rx.Observable;

public interface AuthenticationViewModel {
  Var<String> url();
  Observable<Boolean> isSuccessful();
  Observable<Boolean> verify(Object param);
}