package com.skedgo.android.tripkit.booking.ui.viewmodel

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

enum class Nothing {
  INSTANCE
}

abstract class DisposableViewModel {
  private val _onDispose = PublishSubject.create<Nothing>()

  fun onDispose(): Observable<Nothing> {
    return _onDispose.hide()
  }

  @CallSuper fun dispose() {
    _onDispose.onNext(Nothing.INSTANCE)
  }
}
