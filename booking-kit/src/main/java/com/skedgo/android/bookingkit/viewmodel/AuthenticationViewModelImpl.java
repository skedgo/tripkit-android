package com.skedgo.android.bookingkit.viewmodel;

import com.skedgo.android.common.rx.Var;

import rx.Observable;
import rx.Subscriber;

public final class AuthenticationViewModelImpl implements AuthenticationViewModel {
  private final Var<Boolean> isSuccessful = Var.create(false);
  private final Var<String> url = Var.create();

  @Override
  public Var<String> url() {
    return url;
  }

  @Override
  public Observable<Boolean> isSuccessful() {
    return isSuccessful.observe();
  }

  @Override
  public Observable<Boolean> verify(final Object param) {
    return Observable
        .create(new Observable.OnSubscribe<Boolean>() {
          @Override
          public void call(Subscriber<? super Boolean> subscriber) {
            final String url = String.valueOf(param);
            subscriber.onNext(url != null && url.contains("skedgo.com"));
            subscriber.onCompleted();
          }
        })
        .doOnNext(isSuccessful);
  }
}