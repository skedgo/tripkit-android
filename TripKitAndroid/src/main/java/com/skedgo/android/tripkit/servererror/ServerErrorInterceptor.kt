package com.skedgo.android.tripkit.servererror

import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException
import javax.inject.Inject

internal class ServerErrorInterceptor @Inject constructor(
    private val notifyServerError: NotifyServerError
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response? {

    var response: Response? = null
    try {
      val request = chain.request()
      response = chain.proceed(request)

      when (response.code()) {
        500 -> notifyServerError.notifyServerError(ServerError.InternalServerError)
      }

    } catch (exception: UnknownHostException) {
      notifyServerError.notifyServerError(ServerError.UnknownHost)
    } catch (exception: Exception) {
      notifyServerError.notifyServerError(ServerError.Other)
    }

    return response

  }
}