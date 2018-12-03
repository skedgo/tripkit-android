package com.skedgo.android.tripkit.booking.ui.viewmodel

import androidx.annotation.CallSuper
import rx.Observable
import rx.subjects.PublishSubject

abstract class DisposableViewModel {
  private val _onDispose = PublishSubject.create<Void>()

  fun onDispose(): Observable<Void> {
    return _onDispose.asObservable()
  }

  @CallSuper fun dispose() {
    _onDispose.onNext(null)
  }
}
