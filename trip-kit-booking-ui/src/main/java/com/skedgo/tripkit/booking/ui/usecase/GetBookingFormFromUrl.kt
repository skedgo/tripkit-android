package com.skedgo.tripkit.booking.ui.usecase

import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.BookingService
import io.reactivex.Observable
import javax.inject.Inject

open class GetBookingFormFromUrl @Inject constructor(
    private val bookingService: BookingService
) {

  open fun execute(url: String): Observable<BookingForm> {
    return bookingService.getFormAsync(url).toObservable()
  }

}