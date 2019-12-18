package com.skedgo.tripkit.booking.viewmodel;

import com.skedgo.tripkit.common.rx.Var;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public final class AuthenticationViewModelImpl implements AuthenticationViewModel {
  private final Var<Boolean> isSuccessful = Var.create(false);
  private final Var<String> url = Var.create();

  @Override
  public Var<String> url() {
    return url;
  }

  @Override
  public Observable<Boolean> isSuccessful() {
    return isSuccessful.observe().toObservable();
  }

  @Override
  public Observable<Boolean> verify(final Object param) {
    return Observable
        .create(new ObservableOnSubscribe<Boolean>() {
          @Override
          public void subscribe(ObservableEmitter<Boolean> subscriber) {
            final String url = String.valueOf(param);
            subscriber.onNext(url != null && url.contains("skedgo.com"));
            subscriber.onComplete();
          }
        })
        .doOnNext(isSuccessful);
  }
}