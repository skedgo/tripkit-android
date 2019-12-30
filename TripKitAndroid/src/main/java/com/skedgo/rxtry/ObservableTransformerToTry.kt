package com.skedgo.rxtry

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

internal class ObservableTransformerToTry<T>(
    private val predicate: (Throwable) -> Boolean
) : ObservableTransformer<T, Try<T>> {
  override fun apply(upstream: Observable<T>): Observable<Try<T>> {
    return upstream
        .map<Try<T>> { Success(it) }
        .onErrorResumeNext { error: Throwable ->
          when (predicate(error)) {
            true -> Observable.just(Failure(error))
            false -> Observable.error(error)
          }
        }
  }
}