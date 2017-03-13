package com.skedgo.android.tripkit.booking.ui.usecase

import com.skedgo.android.tripkit.booking.BookingForm
import javax.inject.Inject


open class IsCancelAction @Inject constructor() {

  fun execute(bookingForm: BookingForm?): Boolean {
    val bookingStatusField = bookingForm?.form?.firstOrNull()?.fields?.firstOrNull()

    return bookingStatusField?.value == "Cancelled"

  }

}