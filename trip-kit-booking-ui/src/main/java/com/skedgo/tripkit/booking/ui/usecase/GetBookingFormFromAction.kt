package com.skedgo.tripkit.booking.ui.usecase

import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.BookingService
import com.skedgo.tripkit.booking.InputForm
import io.reactivex.Observable
import javax.inject.Inject

open class GetBookingFormFromAction @Inject constructor(
    private val bookingService: BookingService
) {

  open fun execute(bookingForm: BookingForm): Observable<BookingForm> {
    return bookingService.postFormAsync(bookingForm.action!!.url, InputForm.from(bookingForm.form)).toObservable()
  }

}