package com.skedgo.android.tripkit.booking

import com.google.gson.Gson
import retrofit2.Response
import rx.Observable
import rx.functions.Func1
import java.io.IOException

class BookingServiceImpl(private val bookingApi: BookingApi, private val gson: Gson) : BookingService {
  var handleBookingResponse: Func1<Response<BookingForm>, Observable<BookingForm>> = Func1 { bookingFormResponse ->
    if (!bookingFormResponse.isSuccessful) {
      try {
        val e = asBookingError(bookingFormResponse.errorBody().string())
        return@Func1 Observable.error(e)
      } catch (e: IOException) {
        return@Func1 Observable.error(e)
      }

    } else {
      if (bookingFormResponse.code() == 204) {
        Observable.just(NullBookingForm() as BookingForm)
      } else {
        Observable.just(bookingFormResponse.body())
      }
    }
  }

  override fun getFormAsync(url: String): Observable<BookingForm> {
    return bookingApi.getFormAsync(url).flatMap(handleBookingResponse)
  }

  override fun postFormAsync(url: String, inputForm: InputForm): Observable<BookingForm> = bookingApi.postFormAsync(url, inputForm)
      .flatMap(handleBookingResponse)

  fun asBookingError(bookingErrorJson: String): BookingError {
    return gson.fromJson(bookingErrorJson, BookingError::class.java)
  }
}
