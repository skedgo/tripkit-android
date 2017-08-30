package com.skedgo.android.tripkit.servererror

import rx.Observable
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ObserveNotifyServerError : NotifyServerError, ObserveServerError {

  private val serverErrorPublisher: PublishSubject<ServerError> = PublishSubject.create()

  override fun notifyServerError(serverError: ServerError) {
    serverErrorPublisher.onNext(serverError)
  }

  override fun observeServerError(): Observable<ServerError> =
      serverErrorPublisher.asObservable().debounce(3, TimeUnit.SECONDS)
}