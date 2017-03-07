package com.skedgo.android.tripkit.booking.ui.usecase

import com.skedgo.android.tripkit.booking.BookingForm
import javax.inject.Inject


class IsDoneAction @Inject constructor(
    private val isCancelAction: IsCancelAction
) {

  fun execute(bookingForm: BookingForm?): Boolean {
    return bookingForm?.action?.url == null && !isCancelAction.execute(bookingForm)
  }

}