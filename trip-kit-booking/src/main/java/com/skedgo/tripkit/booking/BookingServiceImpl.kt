package com.skedgo.tripkit.booking

import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import retrofit2.Response
import io.reactivex.Observable
import io.reactivex.functions.Function
import java.io.IOException

class BookingServiceImpl(private val bookingApi: BookingApi, private val gson: Gson) : BookingService {
  var handleBookingResponse: Function<Response<BookingForm>, Observable<BookingForm>> =
      Function { bookingFormResponse ->
        if (!bookingFormResponse.isSuccessful) {
          try {
            val e = asBookingError(bookingFormResponse.errorBody()!!.string())
            return@Function Observable.error(e)
          } catch (e: IOException) {
            return@Function Observable.error(e)
          }

        } else {
          if (bookingFormResponse.code() == 204) {
            Observable.just(NullBookingForm as BookingForm)
          } else {
            Observable.just(bookingFormResponse.body()!!)
          }
        }
      }

  override fun getFormAsync(url: String): Flowable<BookingForm> {
    return bookingApi.getFormAsync(url).flatMap(handleBookingResponse).toFlowable(BackpressureStrategy.BUFFER)
  }

  override fun postActionInputAsync(url: String, field: String, value: String): Flowable<BookingForm> =
    bookingApi.postFormAsync(url, ActionInputForm(listOf(ActionInputFormField(field, value))))
            .flatMap (handleBookingResponse).toFlowable(BackpressureStrategy.BUFFER)

  override fun postFormAsync(url: String, inputForm: InputForm?): Flowable<BookingForm>{
    return if (inputForm != null) {
      bookingApi.postFormAsync(url, inputForm)
              .flatMap(handleBookingResponse).toFlowable(BackpressureStrategy.BUFFER)
    } else {
      bookingApi.postFormAsync(url)
              .flatMap(handleBookingResponse).toFlowable(BackpressureStrategy.BUFFER)
    }
  }

  fun asBookingError(bookingErrorJson: String): BookingError {
    return gson.fromJson(bookingErrorJson, BookingError::class.java)
  }
}
