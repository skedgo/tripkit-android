package com.skedgo.android.tripkit.booking.ui.usecase

import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.BookingService
import rx.Observable
import javax.inject.Inject

class GetBookingFormFromUrl @Inject constructor(
    private val bookingService: BookingService
) {

  fun execute(url: String): Observable<BookingForm> {
    return bookingService.getFormAsync(url)
  }


}