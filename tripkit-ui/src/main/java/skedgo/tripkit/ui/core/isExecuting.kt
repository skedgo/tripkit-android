package skedgo.tripkit.ui.core

import rx.Observable

fun <T> Observable<T>.isExecuting(f: (Boolean) -> Unit): Observable<T> {
  return this.doOnSubscribe { f(true) }
      .doOnTerminate { f(false) }
      .doOnUnsubscribe { f(false) }
}