package com.skedgo.android.tripkit.servererror

import rx.Observable

interface ObserveServerError {

  fun observeServerError(): Observable<ServerError>

}