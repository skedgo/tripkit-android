package com.skedgo.android.tripkit.booking.ui.usecase

import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.BookingService
import com.skedgo.android.tripkit.booking.InputForm
import rx.Observable
import javax.inject.Inject

open class GetBookingFormFromAction @Inject constructor(
    private val bookingService: BookingService
) {

  open fun execute(bookingForm: BookingForm): Observable<BookingForm> {
    return bookingService.postFormAsync(bookingForm.action!!.url, InputForm.from(bookingForm.form))
  }

}