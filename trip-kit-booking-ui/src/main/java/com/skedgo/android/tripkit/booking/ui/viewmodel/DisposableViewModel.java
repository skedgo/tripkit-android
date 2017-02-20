package com.skedgo.android.tripkit.booking.ui.viewmodel;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class DisposableViewModel {
  private final PublishSubject<Void> _onDispose = PublishSubject.create();

  public final @NonNull Observable<Void> onDispose() {
    return _onDispose.asObservable();
  }

  @CallSuper public void dispose() {
    _onDispose.onNext(null);
  }
}
