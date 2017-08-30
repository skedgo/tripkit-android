package com.skedgo.android.tripkit.servererror

internal interface NotifyServerError {

  fun notifyServerError(serverError: ServerError)

}