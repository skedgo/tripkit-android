package com.skedgo.tripkit.booking.viewmodel;

import com.skedgo.tripkit.common.rx.Var;
import io.reactivex.Observable;

public interface AuthenticationViewModel {
  Var<String> url();
  Observable<Boolean> isSuccessful();
  Observable<Boolean> verify(Object param);
}